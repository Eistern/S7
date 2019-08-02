package com.contacts.demo;

import com.contacts.demo.data.NameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/", produces = "application/json")
@CrossOrigin
public class IndexController {
    private final Logger log = Logger.getLogger(IndexController.class.getName());
    private NameRepository nameRepository;

    @GetMapping
    public String index() {
        return "index";
    }

    @Autowired
    public IndexController(NameRepository nameRepository) {
        this.nameRepository = nameRepository;
    }

    @PostMapping
    public NewContact addEntry(@Valid NewContact newContact, Errors errors) {
        if (errors.hasErrors())
            log.info("Input error");
        else {
            log.info("New contact created " + newContact);
            nameRepository.save(newContact.getName());
        }
        return newContact;
    }
}
