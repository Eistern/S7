package com.contacts.demo.controllers;

import com.contacts.demo.data.JpaNumberRepository;
import com.contacts.demo.data.jdbcRepositories.IdRepository;
import com.contacts.demo.data.types.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/number", produces = "application/json")
@CrossOrigin
public class NumberController {
    private final IdRepository<PhoneNumber> numberRepository;
    private final JpaNumberRepository numberRepositoryJPA;
    private final Logger log = Logger.getLogger(NumberController.class.getName());

    @Autowired
    public NumberController(IdRepository<PhoneNumber> numberRepository, JpaNumberRepository jpaNumberRepository) {
        this.numberRepository = numberRepository;
        this.numberRepositoryJPA = jpaNumberRepository;
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

    //TODO
    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<PhoneNumber> editNumber(@PathVariable("id") Integer id, @RequestBody PhoneNumber newNumber) {
        if (!id.equals(newNumber.getId()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        numberRepository.update(id, newNumber);
        log.info("EditNumber executed. Id=" + id + " updated. Now: " + newNumber);
        return new ResponseEntity<>(newNumber, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Integer id) {
        numberRepositoryJPA.deleteById(id);
        log.info("DeleteById executed. Id=" + id + " deleted");
    }
}
