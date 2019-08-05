package com.contacts.demo.controllers;

import com.contacts.demo.data.IdRepository;
import com.contacts.demo.data.types.Person;
import com.contacts.demo.data.types.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/personal", produces = "application/json")
public class PersonalDataController {
    private IdRepository<Person> personRepository;
    private IdRepository<PhoneNumber> numberRepository;

    @Autowired
    public PersonalDataController(IdRepository<Person> personRepository, IdRepository<PhoneNumber> numberRepository) {
        this.numberRepository = numberRepository;
        this.personRepository = personRepository;
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
}
