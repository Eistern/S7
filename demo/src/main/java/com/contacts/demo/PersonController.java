package com.contacts.demo;

import com.contacts.demo.data.IdRepository;
import com.contacts.demo.data.types.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/person", produces = "application/json")
@CrossOrigin("*")
public class PersonController {
    private IdRepository<Person> nameRepository;
    private final Logger log = Logger.getLogger(PersonController.class.getName());

    @Autowired
    public PersonController(IdRepository<Person> nameRepository) {
        this.nameRepository = nameRepository;
    }

    @GetMapping
    public Iterable<Person> showPerson() {
        Iterable<Person> result = nameRepository.findAll();
        log.info("ShowPerson executed");
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

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Integer id) {
        nameRepository.deleteById(id);
        log.info("DeleteById executed. Id=" + id + "deleted");
        return;
    }
}
