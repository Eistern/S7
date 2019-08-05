package com.contacts.demo.controllers;

import com.contacts.demo.data.IdRepository;
import com.contacts.demo.data.types.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/number", produces = "application/json")
@CrossOrigin
public class NumberController {
    private final IdRepository<PhoneNumber> numberRepository;
    private final Logger log = Logger.getLogger(NumberController.class.getName());

    @Autowired
    public NumberController(IdRepository<PhoneNumber> numberRepository) {
        this.numberRepository = numberRepository;
    }

    @GetMapping
    public Iterable<PhoneNumber> showNumbers() {
        Iterable<PhoneNumber> result = numberRepository.findAll();
        log.info("ShowNumbers executed");
        return result;
    }

    @GetMapping("/{id}")
    public PhoneNumber showNumberById(@PathVariable("id") Integer id) {
        PhoneNumber result = numberRepository.findById(id);
        log.info("ShowNumberById executed with Id=" + id);
        return result;
    }

    @PostMapping
    public PhoneNumber addNumber(@Valid PhoneNumber newNumber) {
        numberRepository.save(newNumber);
        log.info("AddNumber executed " + newNumber + " added");
        return newNumber;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PhoneNumber> editNumber(@PathVariable("id") Integer id, @Valid PhoneNumber newNumber) {
        if (!id.equals(newNumber.getId()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        numberRepository.update(id, newNumber);
        log.info("EditNumber executed. Id=" + id + " updated. Now: " + newNumber);
        return new ResponseEntity<>(newNumber, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Integer id) {
        numberRepository.deleteById(id);
        log.info("DeleteById executed. Id=" + id + " deleted");
    }
}
