package com.contacts.demo.controllers;

import com.contacts.demo.data.JpaNameRepository;
import com.contacts.demo.data.JpaNumberRepository;
import com.contacts.demo.data.types.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/number", produces = "application/json")
@CrossOrigin("*")
public class NumberController {
    private final JpaNumberRepository numberRepositoryJPA;
    private final JpaNameRepository nameRepositoryJPA;
    private final Logger log = Logger.getLogger(NumberController.class.getName());

    @Autowired
    public NumberController(JpaNameRepository jpaNameRepository, JpaNumberRepository jpaNumberRepository) {
        this.numberRepositoryJPA = jpaNumberRepository;
        this.nameRepositoryJPA = jpaNameRepository;
    }

    @GetMapping
    public Iterable<PhoneNumber> showNumbers() {
        Iterable<PhoneNumber> result = numberRepositoryJPA.findAll();
        log.info("ShowNumbers executed");
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhoneNumber> showNumberById(@PathVariable("id") @NumberFormat Integer id) {
        Optional<PhoneNumber> result;
        if ((result = numberRepositoryJPA.findById(id)).isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        log.info("ShowNumberById executed with Id=" + id);
        return new ResponseEntity<>(result.get(), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public PhoneNumber addNumber(@RequestBody @Valid PhoneNumber newNumber) {
        numberRepositoryJPA.save(newNumber);
        log.info("AddNumber executed " + newNumber + " added");
        return newNumber;
    }

    @Transactional
    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<PhoneNumber> editNumber(@PathVariable("id") Integer id, @RequestBody @NotEmpty PhoneNumber newNumber) {
        if (!numberRepositoryJPA.existsById(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (newNumber.getPersonId() != null && !nameRepositoryJPA.existsById(newNumber.getPersonId()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        PhoneNumber editableNumber = numberRepositoryJPA.findById(id).get();
        String correctedNumber = editableNumber.getPhoneNumber().trim();
        editableNumber.setPhoneNumber(correctedNumber);

        if (newNumber.getPersonId() != null)
            editableNumber.setPersonId(newNumber.getPersonId());
        if (newNumber.getPhoneNumber() != null)
            editableNumber.setPhoneNumber(newNumber.getPhoneNumber());
        PhoneNumber resultedNumber = numberRepositoryJPA.save(editableNumber);

        log.info("EditNumber executed. Id=" + id + " updated. Now: " + resultedNumber);
        return new ResponseEntity<>(resultedNumber, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Integer id) {
        numberRepositoryJPA.deleteById(id);
        log.info("DeleteById executed. Id=" + id + " deleted");
    }
}
