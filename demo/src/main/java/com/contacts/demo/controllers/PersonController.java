package com.contacts.demo.controllers;

import com.contacts.demo.data.JpaNameRepository;
import com.contacts.demo.data.types.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/person", produces = "application/json")
@CrossOrigin("*")
public class PersonController {
    private final JpaNameRepository nameRepositoryJPA;
    private final Logger log = Logger.getLogger(PersonController.class.getName());

    @Autowired
    public PersonController(JpaNameRepository nameRepositoryJPA) {
        this.nameRepositoryJPA = nameRepositoryJPA;
    }

    @GetMapping
    public Iterable<Person> showPersons() {
        Iterable<Person> result = nameRepositoryJPA.findAll();
        log.info("ShowPersons executed");
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> showPersonById(@PathVariable("id") Integer id) {
        Optional<Person> result;
        if ((result = nameRepositoryJPA.findById(id)).isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        log.info("ShowPersonById executed with id=" + id);
        return new ResponseEntity<>(result.get(), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public Person addPerson(@RequestBody @Valid Person newPerson) {
        nameRepositoryJPA.save(newPerson);
        log.info("AddPerson executed. " + newPerson + " added");
        return newPerson;
    }

    @Transactional
    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<Person> editPerson(@PathVariable("id") Integer id, @RequestBody @NotEmpty Person newPerson) {
        if (!nameRepositoryJPA.existsById(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Person editablePerson = nameRepositoryJPA.findById(id).get();
        String correctName = editablePerson.getName().trim();
        editablePerson.setName(correctName);

        if (newPerson.getName() != null)
            editablePerson.setName(newPerson.getName());
        Person resultedPerson = nameRepositoryJPA.save(editablePerson);

        log.info("EditPerson executed. Id=" + id + " updated. Now :" + newPerson);
        return new ResponseEntity<>(resultedPerson, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Integer id) {
        nameRepositoryJPA.deleteById(id);
        log.info("DeleteById executed. Id=" + id + " deleted");
    }
}
