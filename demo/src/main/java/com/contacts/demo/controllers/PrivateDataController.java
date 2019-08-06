package com.contacts.demo.controllers;

import com.contacts.demo.data.CollapsingRepository;
import com.contacts.demo.data.IdRepository;
import com.contacts.demo.data.types.Person;
import com.contacts.demo.data.types.PhoneNumber;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/private", produces = "application/json")
public class PrivateDataController {
    private CollapsingRepository<Person, PhoneNumber> merged;
    private IdRepository<Person> personRepository;
    private IdRepository<PhoneNumber> numberRepository;

    @Autowired
    public PrivateDataController(IdRepository<Person> personRepository, IdRepository<PhoneNumber> numberRepository, CollapsingRepository<Person, PhoneNumber> merged) {
        this.numberRepository = numberRepository;
        this.personRepository = personRepository;
        this.merged = merged;
    }

    @GetMapping("/{pid}")
    public Iterable<PhoneNumber> showNumbers(@PathVariable("pid") Integer pid) {
        return numberRepository.findByPid(pid);
    }

    @GetMapping("/number")
    public Iterable<PhoneNumber> showPrivateNumbers() {
        return numberRepository.findSecureAll();
    }

    @GetMapping("/person")
    public Iterable<Person> showPrivateNames() {
        return personRepository.findSecureAll();
    }

    @GetMapping("/contacts")
    public Iterable<Pair<Person, PhoneNumber>> collapseTables() {
        return merged.mergeData();
    }
}
