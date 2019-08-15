package com.contacts.demo.controllers;

import com.contacts.demo.data.JpaNameRepository;
import com.contacts.demo.data.JpaNumberRepository;
import com.contacts.demo.data.types.PhoneNumber;
import com.contacts.demo.kafka.MessageTypes;
import com.contacts.demo.kafka.NumberUpdateMessage;
import com.contacts.demo.security.data.types.UserEntry;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/number", produces = "application/json")
@CrossOrigin("*")
public class NumberController {
    private final static String TOPIC = "phone-update";
    private final JpaNumberRepository numberRepositoryJPA;
    private final JpaNameRepository nameRepositoryJPA;
    private final Logger log = Logger.getLogger(NumberController.class.getName());
    private final KafkaTemplate<String, NumberUpdateMessage> kafkaUpdate;
    private final HazelcastInstance hazelcastInstance;


    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public NumberController(JpaNameRepository jpaNameRepository, JpaNumberRepository jpaNumberRepository, KafkaTemplate<String, NumberUpdateMessage> kafkaUpdate, HazelcastInstance hazelcastInstance) {
        this.numberRepositoryJPA = jpaNumberRepository;
        this.nameRepositoryJPA = jpaNameRepository;
        this.kafkaUpdate = kafkaUpdate;
        this.hazelcastInstance = hazelcastInstance;
    }

    @GetMapping
    public Iterable<PhoneNumber> showNumbers(@AuthenticationPrincipal UserEntry userEntry) {
        Iterable<PhoneNumber> result = numberRepositoryJPA.findAllByOwnerUid(userEntry.getUid());
        log.info("ShowNumbers executed");
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhoneNumber> showNumberById(@PathVariable("id") @NumberFormat Integer id, @AuthenticationPrincipal UserEntry userEntry) {
        Optional<PhoneNumber> result = numberRepositoryJPA.findByPhoneIdAndOwnerUid(id, userEntry.getUid());
        if (result.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        kafkaUpdate.send(TOPIC, new NumberUpdateMessage(MessageTypes.ACCESS, userEntry.getUid(), result.get()));
        incrementProduced();
        log.info("ShowNumberById executed with Id=" + id);
        return new ResponseEntity<>(result.get(), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public PhoneNumber addNumber(@RequestBody @Valid PhoneNumber newNumber, @AuthenticationPrincipal UserEntry userEntry) {
        numberRepositoryJPA.save(newNumber);

        kafkaUpdate.send(TOPIC, new NumberUpdateMessage(MessageTypes.CREATED, userEntry.getUid(), newNumber));
        incrementProduced();
        log.info("AddNumber executed " + newNumber + " added");
        return newNumber;
    }

    @Transactional
    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<PhoneNumber> editNumber(@PathVariable("id") @NumberFormat Integer id, @RequestBody PhoneNumber newNumber, @AuthenticationPrincipal UserEntry userEntry) {
        Optional<PhoneNumber> foundNumber = numberRepositoryJPA.findByPhoneIdAndOwnerUid(id, userEntry.getUid());
        if (foundNumber.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        PhoneNumber editableNumber = foundNumber.get();
        String correctedNumber = editableNumber.getPhoneNumber();

        editableNumber.setPhoneNumber(correctedNumber);

        if (newNumber.getPersonId() != null) {
            if (!nameRepositoryJPA.existsByPersonIdAndUid(newNumber.getPersonId(), userEntry.getUid()))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            else
                editableNumber.setPersonId(newNumber.getPersonId());
        }

        if (newNumber.getPhoneNumber() != null)
            editableNumber.setPhoneNumber(newNumber.getPhoneNumber());
        PhoneNumber resultedNumber = numberRepositoryJPA.save(editableNumber);

        kafkaUpdate.send(TOPIC, new NumberUpdateMessage(MessageTypes.UPDATE, userEntry.getUid(), resultedNumber));
        incrementProduced();
        log.info("EditNumber executed. Id=" + id + " updated. Now: " + resultedNumber);
        return new ResponseEntity<>(resultedNumber, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable("id") @NumberFormat Integer id, @AuthenticationPrincipal UserEntry userEntry) {
        if (!numberRepositoryJPA.existsByPhoneIdAndOwnerUid(id, userEntry.getUid()))
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        numberRepositoryJPA.deleteById(id);

        kafkaUpdate.send(TOPIC, new NumberUpdateMessage(MessageTypes.DELETE, userEntry.getUid(), new PhoneNumber(id)));
        incrementProduced();
        log.info("DeleteById executed. Id=" + id + " deleted");
        return new ResponseEntity(HttpStatus.OK);
    }

    private void incrementProduced() {
        Map<String, Integer> kafkaCount = hazelcastInstance.getMap("kafka-count");
        Integer sentCount = kafkaCount.getOrDefault("produced-number", 0);
        kafkaCount.put("produced-number", sentCount + 1);
    }
}
