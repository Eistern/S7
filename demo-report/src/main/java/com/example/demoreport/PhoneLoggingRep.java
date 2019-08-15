package com.example.demoreport;

import com.contacts.demo.kafka.NumberUpdateMessage;
import org.springframework.data.repository.CrudRepository;

public interface PhoneLoggingRep extends CrudRepository<NumberUpdateMessage, Integer> {
}
