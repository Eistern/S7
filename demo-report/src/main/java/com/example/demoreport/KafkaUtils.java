package com.example.demoreport;

import com.example.demoreport.types.NumberUpdateMessage;
import com.example.demoreport.types.PersonUpdateMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Service
public class KafkaUtils {
    private final KafkaConsumer<String, PersonUpdateMessage> personConsumer;
    private final KafkaConsumer<String, NumberUpdateMessage> numberConsumer;

    public KafkaUtils() throws IOException {
        this.personConsumer = getPersonConsumer();
        this.personConsumer.subscribe(Collections.singletonList("person-update"));
        this.numberConsumer = getNumberConsumer();
        this.numberConsumer.subscribe(Collections.singletonList("phone-update"));
    }

    public List<PersonUpdateMessage> getListOfPersonUpdates() {
        ConsumerRecords<String, PersonUpdateMessage> records = personConsumer.poll(Duration.ofSeconds(10));
        List<PersonUpdateMessage> result = new ArrayList<>();
        records.forEach(record -> result.add(record.value()));
        return result;
    }

    public List<NumberUpdateMessage> getArrayOfNumberUpdates() {
        ConsumerRecords<String, NumberUpdateMessage> records = numberConsumer.poll(Duration.ofSeconds(10));
        List<NumberUpdateMessage> result = new ArrayList<>();
        records.forEach(record -> result.add(record.value()));
        return result;
    }

    @Bean KafkaConsumer<String, NumberUpdateMessage> getNumberConsumer() throws IOException {
        Properties consumerProps = getDefaultConsumerProperties();
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "number-group");
        consumerProps.put(JsonDeserializer.DEFAULT_VALUE_TYPE, NumberUpdateMessage.class);

        return new KafkaConsumer<>(consumerProps);
    }

    @Bean
    public KafkaConsumer<String, PersonUpdateMessage> getPersonConsumer() throws IOException {
        Properties consumerProps = getDefaultConsumerProperties();
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "person-group");
        consumerProps.put(JsonDeserializer.DEFAULT_VALUE_TYPE, PersonUpdateMessage.class);

        return new KafkaConsumer<>(consumerProps);
    }

    @Bean
    public Properties getDefaultConsumerProperties() throws IOException {
        Properties consumerProps = new Properties();
        InputStream stream = this.getClass().getResourceAsStream("/kafka.properties");
        consumerProps.load(stream);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        consumerProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return consumerProps;
    }
}
