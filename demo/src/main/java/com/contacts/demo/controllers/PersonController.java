package com.contacts.demo.controllers;

import com.contacts.demo.data.JpaNameRepository;
import com.contacts.demo.data.types.Person;
import com.contacts.demo.security.data.types.UserEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public Iterable<Person> showPersons(@AuthenticationPrincipal UserEntry userEntry) {
        Iterable<Person> result = nameRepositoryJPA.findAllByUid(userEntry.getUid());
        log.info("ShowPersons executed");
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> showPersonById(@PathVariable("id") @NumberFormat Integer id, @AuthenticationPrincipal UserEntry userEntry) {
        Optional<Person> result;
        if ((result = nameRepositoryJPA.findByPersonIdAndUid(id, userEntry.getUid())).isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        log.info("ShowPersonById executed with id=" + id);
        return new ResponseEntity<>(result.get(), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public Person addPerson(@RequestBody @Valid Person newPerson, @AuthenticationPrincipal UserEntry userEntry) {
        newPerson.setUid(userEntry.getUid());
        nameRepositoryJPA.save(newPerson);
        log.info("AddPerson executed. " + newPerson + " added");
        return newPerson;
    }

    @Transactional
    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<Person> editPerson(@PathVariable("id") @NumberFormat Integer id, @RequestBody @NotEmpty Person newPerson, @AuthenticationPrincipal UserEntry userEntry) {
        Optional<Person> foundPerson = nameRepositoryJPA.findByPersonIdAndUid(id, userEntry.getUid());
        if (foundPerson.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Person editablePerson = foundPerson.get();
        String correctName = editablePerson.getName();
        editablePerson.setName(correctName);

        if (newPerson.getName() != null)
            editablePerson.setName(newPerson.getName());
        Person resultedPerson = nameRepositoryJPA.save(editablePerson);

        log.info("EditPerson executed. Id=" + id + " updated. Now :" + newPerson);
        return new ResponseEntity<>(resultedPerson, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable("id") @NumberFormat Integer id, @AuthenticationPrincipal UserEntry userEntry) {
        if (!nameRepositoryJPA.existsByPersonIdAndUid(id, userEntry.getUid()))
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        nameRepositoryJPA.deleteById(id);
        log.info("DeleteById executed. Id=" + id + " deleted");
        return new ResponseEntity(HttpStatus.OK);
    }
}
