package com.contacts.demo.controllers;

import com.contacts.demo.security.RoleHelper;
import com.contacts.demo.security.data.JpaUserRepository;
import com.contacts.demo.security.data.types.Role;
import com.contacts.demo.security.data.types.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/user", produces = "application/json")
@CrossOrigin("*")
public class UserController {
    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleHelper roleHelper;

    @Autowired
    public UserController(JpaUserRepository userRepository, PasswordEncoder passwordEncoder, RoleHelper roleHelper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleHelper = roleHelper;
    }

    @GetMapping
    public String plugMethod() {
        return "You can authorize or register by /user/login or /user/register";
    }

    @GetMapping(path = "/list")
    public Iterable<User> users() {
        return userRepository.findAll();
    }

    @PostMapping(path = "/login", consumes = "application/json")
    public String loginRequest() {
        return null;
    }

    @PostMapping(path = "/register", consumes = "application/json")
    public ResponseEntity<String> registerRequest(@RequestBody @Valid User user) {
        if (userRepository.existsByUsername(user.getUsername()))
            return new ResponseEntity<>("Username already taken", HttpStatus.BAD_REQUEST);

        String encoded = passwordEncoder.encode(user.getPassword());
        user.setPassword(encoded);
        userRepository.save(user);
        User createdUser = userRepository.findByUsername(user.getUsername()).get();
        roleHelper.addRoleTo(createdUser.getUserId(), Role.Roles.USER_ROLE);
//        Integer returnedUID = userRepository.returnValue(user.getUsername(), user.getPassword());
//        roleHelper.addRoleTo(returnedUID, Role.Roles.USER_ROLE);
        return new ResponseEntity<>("User created", HttpStatus.OK);
    }
}
