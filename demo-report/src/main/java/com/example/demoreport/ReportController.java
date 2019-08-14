package com.example.demoreport;

import com.example.demoreport.types.NumberUpdateMessage;
import com.example.demoreport.types.PersonUpdateMessage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(produces = "application/json")
public class ReportController {
    private final KafkaUtils kafkaUtils;

    @Autowired
    public ReportController(KafkaUtils kafkaUtils) {
        this.kafkaUtils = kafkaUtils;
    }

    @GetMapping(path = "/phone")
    public String generateNumberReport() throws JRException, IOException {
        List<NumberUpdateMessage> result = kafkaUtils.getArrayOfNumberUpdates();
        InputStream stream = this.getClass().getResourceAsStream("/phoneReport.jrxml");
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(result);
        byte[] pdf = compileToPdf(stream, dataSource);
        Files.write(Paths.get("number.pdf"), pdf);

        return "Done!";
    }

    @GetMapping(path = "/person")
    public String generatePersonReport() throws JRException, IOException {
        List<PersonUpdateMessage> result = kafkaUtils.getListOfPersonUpdates();
        InputStream stream = this.getClass().getResourceAsStream("/personReport.jrxml");
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
}
