package com.contacts.demo.controllers;

import com.contacts.demo.data.IdRepository;
import com.contacts.demo.data.types.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/person", produces = "application/json")
@CrossOrigin("*")
public class PersonController {
    private final IdRepository<Person> nameRepository;
    private final Logger log = Logger.getLogger(PersonController.class.getName());

    @Autowired
    public PersonController(IdRepository<Person> nameRepository) {
        this.nameRepository = nameRepository;
    }

    @GetMapping
    public Iterable<Person> showPersons() {
        Iterable<Person> result = nameRepository.findAll();
        log.info("ShowPersons executed");
        return result;
    }

    @GetMapping("/{id}")
    public Person showPersonById(@PathVariable("id") Integer id) {
        Person result = nameRepository.findOne(id);
        log.info("ShowPersonById executed with id=" + id);
        return result;
    }

    @PostMapping
    public Person addPerson(@Valid Person newPerson) {
        nameRepository.save(newPerson);
        log.info("AddPerson executed. " + newPerson + " added");
        return newPerson;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Person> editPerson(@PathVariable("id") Integer id, @Valid Person newPerson) {
        if (!id.equals(newPerson.getId()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        nameRepository.update(id, newPerson);
        log.info("EditPerson executed. Id=" + id + " updated. Now :" + newPerson);
        return new ResponseEntity<>(newPerson, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Integer id) {
        nameRepository.deleteById(id);
        log.info("DeleteById executed. Id=" + id + " deleted");
    }
}
