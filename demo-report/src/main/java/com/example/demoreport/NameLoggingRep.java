package com.example.demoreport;

import com.contacts.demo.kafka.PersonUpdateMessage;
import org.springframework.data.repository.CrudRepository;

public interface NameLoggingRep extends CrudRepository<PersonUpdateMessage, Integer> {
}
