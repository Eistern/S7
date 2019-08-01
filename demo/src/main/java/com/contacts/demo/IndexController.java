package com.contacts.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public ModelAndView index() {
        Map<String, String> model = new HashMap<>();
        model.put("name", "User");
        return new ModelAndView("index", model);
    }
}
