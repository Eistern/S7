package com.contacts.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@Controller
public class IndexController {
    private final Logger log = Logger.getLogger(IndexController.class.getName());

    @GetMapping
    public String index() {
        return "index";
    }
}
