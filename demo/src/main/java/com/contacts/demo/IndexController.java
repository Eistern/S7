package com.contacts.demo;

import com.contacts.demo.data.IdRepository;
import com.contacts.demo.data.types.Person;
import com.contacts.demo.data.types.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/", produces = "application/json")
@CrossOrigin("*")
public class IndexController {
    private final Logger log = Logger.getLogger(IndexController.class.getName());
    private IdRepository<Person> nameRepository;

    @GetMapping
    public String index() {
        return "index";
    }

    @Autowired
    public IndexController(IdRepository<Person> nameRepository) {
        this.nameRepository = nameRepository;
    }

    @PostMapping
    public Person addEntry(@Valid Person newPerson, @Valid PhoneNumber phoneNumber, Errors errors) {
        if (errors.hasErrors())
            log.info("Input error");
        else {
            log.info("New contact created " + newPerson + " " + phoneNumber);
            nameRepository.save(newPerson);
        }
        return null;
    }
}
