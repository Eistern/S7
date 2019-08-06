package com.contacts.demo.controllers;

import com.contacts.demo.data.jdbcRepositories.IdRepository;
import com.contacts.demo.data.JpaNameRepository;
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
    private final JpaNameRepository nameRepositoryJPA;
    private final Logger log = Logger.getLogger(PersonController.class.getName());

    @Autowired
    public PersonController(IdRepository<Person> nameRepository, JpaNameRepository nameRepositoryJPA) {
        this.nameRepository = nameRepository;
        this.nameRepositoryJPA = nameRepositoryJPA;
    }

    @GetMapping
    public Iterable<Person> showPersons() {
        Iterable<Person> result = nameRepositoryJPA.findAll();
        log.info("ShowPersons executed");
        return result;
    }

    @GetMapping("/{id}")
    public Person showPersonById(@PathVariable("id") Integer id) {
        Person result = nameRepository.findById(id);
        log.info("ShowPersonById executed with id=" + id);
        return result;
    }

    @PostMapping(consumes = "application/json")
    public Person addPerson(@RequestBody @Valid Person newPerson) {
        nameRepositoryJPA.save(newPerson);
        log.info("AddPerson executed. " + newPerson + " added");
        return newPerson;
    }

    //TODO
    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<Person> editPerson(@PathVariable("id") Integer id, @RequestBody Person newPerson) {
        if (!id.equals(newPerson.getPersonId()) || !nameRepositoryJPA.existsById(id))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        nameRepository.update(id, newPerson);
        log.info("EditPerson executed. Id=" + id + " updated. Now :" + newPerson);
        return new ResponseEntity<>(newPerson, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Integer id) {
        nameRepositoryJPA.deleteById(id);
        log.info("DeleteById executed. Id=" + id + " deleted");
    }
}
