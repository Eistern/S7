package com.example.demoreport;

import com.contacts.demo.kafka.NumberUpdateMessage;
import com.contacts.demo.kafka.PersonUpdateMessage;
import com.hazelcast.core.HazelcastInstance;
import com.zaxxer.hikari.HikariDataSource;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(produces = "application/json")
public class ReportController {
    private final KafkaUtils kafkaUtils;
    private final NameLoggingRep nameRep;
    private final PhoneLoggingRep phoneRep;
    private final HikariDataSource dataSource;
    private final HazelcastInstance hazelcastInstance;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public ReportController(KafkaUtils kafkaUtils, NameLoggingRep nameRep, PhoneLoggingRep phoneRep, HikariDataSource dataSource, HazelcastInstance hazelcastInstance) {
        this.kafkaUtils = kafkaUtils;
        this.nameRep = nameRep;
        this.phoneRep = phoneRep;
        this.dataSource = dataSource;
        this.hazelcastInstance = hazelcastInstance;
    }

    @KafkaListener(topics = "person-update", clientIdPrefix = "listener", groupId = "person-listeners2")
    public void personUpdated(@Payload PersonUpdateMessage updateMessage, @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long time) {
        System.out.println(updateMessage.setTimestamp(new Date(time)));
        updateMessage.setName(updateMessage.getName());
        updateMessage.setPersonId(updateMessage.getPersonId());
        nameRep.save(updateMessage);

        Map<String, Integer> kafkaCount = hazelcastInstance.getMap("kafka-count");
        Integer count = kafkaCount.getOrDefault("consumed-person", 0);
        kafkaCount.put("consumed-person", count + 1);
    }

    @KafkaListener(topics = "phone-update", clientIdPrefix = "listener", groupId = "phone-listeners2")
    public void phoneUpdated(@Payload NumberUpdateMessage updateMessage, @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long time) {
        System.out.println(updateMessage.setTimestamp(new Date(time)));
        updateMessage.setPhoneId(updateMessage.getPhoneId());
        updateMessage.setPersonId(updateMessage.getPersonId());
        updateMessage.setPhoneNumber(updateMessage.getPhoneNumber());
        phoneRep.save(updateMessage);

        Map<String, Integer> kafkaCount = hazelcastInstance.getMap("kafka-count");
        Integer count = kafkaCount.getOrDefault("consumed-number", 0);
        kafkaCount.put("consumed-number", count + 1);
    }

    @GetMapping(path = "/phone")
    public String generateNumberReport() throws IOException, SQLException, JRException {
        InputStream stream = this.getClass().getResourceAsStream("/phoneReport.jrxml");
        byte[] pdf = compileToPdf(stream, dataSource.getConnection());
        Files.write(Paths.get("number.pdf"), pdf);

        Map<String, Integer> kafkaCount = hazelcastInstance.getMap("kafka-count");
        Integer produced = kafkaCount.getOrDefault("produced-number", 0);
        Integer consumed = kafkaCount.getOrDefault("consumed-number", 0);
        return "Generated report for " + consumed + " out of " + produced + " entries";
    }

    @GetMapping(path = "/person")
    public String generatePersonReport() throws IOException, SQLException, JRException {
        InputStream stream = this.getClass().getResourceAsStream("/personReport.jrxml");
        byte[] pdf = compileToPdf(stream, dataSource.getConnection());
        Files.write(Paths.get("person.pdf"), pdf);

        Map<String, Integer> kafkaCount = hazelcastInstance.getMap("kafka-count");
        Integer produced = kafkaCount.getOrDefault("produced-person", 0);
        Integer consumed = kafkaCount.getOrDefault("consumed-person", 0);
        return "Generated report for " + consumed + " out of " + produced + " entries";
    }

    @GetMapping(path = "/phone/in-memory")
    public String generateNumberReportInMemory() throws JRException, IOException {
        List<NumberUpdateMessage> result = kafkaUtils.getArrayOfNumberUpdates();
        InputStream stream = this.getClass().getResourceAsStream("/beanconsumer/phoneReport.jrxml");
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(result);
        byte[] pdf = compileToPdf(stream, dataSource);
        Files.write(Paths.get("number.pdf"), pdf);

        return "Done!";
    }

    @GetMapping(path = "/person/in-memory")
    public String generatePersonReportInMemory() throws JRException, IOException {
        List<PersonUpdateMessage> result = kafkaUtils.getListOfPersonUpdates();
        InputStream stream = this.getClass().getResourceAsStream("/beanconsumer/personReport.jrxml");
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(result);
        byte[] pdf = compileToPdf(stream, dataSource);
        Files.write(Paths.get("person.pdf"), pdf);

        return "Done!";
    }

    private byte[] compileToPdf(InputStream stream, JRDataSource dataSource) throws JRException {
        JasperReport compiled = JasperCompileManager.compileReport(stream);
        Map<String, Object> params = new HashMap<>();
        JasperPrint printing = JasperFillManager.fillReport(compiled, params, dataSource);
        return JasperExportManager.exportReportToPdf(printing);
    }

    private byte[] compileToPdf(InputStream stream, Connection connection) throws JRException {
        JasperReport compiled = JasperCompileManager.compileReport(stream);
        Map<String, Object> params = new HashMap<>();
        JasperPrint printing = JasperFillManager.fillReport(compiled, params, connection);
        return JasperExportManager.exportReportToPdf(printing);
    }
}
