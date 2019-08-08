package com.contacts.demo.controllers;

import com.contacts.demo.security.JpaUserDetailsService;
import com.contacts.demo.security.RoleHelper;
import com.contacts.demo.security.data.JpaUserRepository;
import com.contacts.demo.security.data.types.Role;
import com.contacts.demo.security.data.types.User;
import com.contacts.demo.security.data.types.UserEntry;
import com.contacts.demo.security.jwt.CookieUtils;
import com.contacts.demo.security.jwt.JwtToken;
import com.contacts.demo.security.jwt.JwtUtils;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/user", produces = "application/json")
@CrossOrigin("*")
public class UserController {
    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleHelper roleHelper;
    private final AuthenticationManager authenticationManager;
    private final JpaUserDetailsService userDetailsService;

    @Autowired
    public UserController(JpaUserRepository userRepository, PasswordEncoder passwordEncoder, RoleHelper roleHelper, JpaUserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleHelper = roleHelper;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
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
    public ResponseEntity<JwtToken> loginRequest(@RequestBody @Valid User user, HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        UserDetails userEntry = userDetailsService.loadUserByUsername(user.getUsername());
        String responseToken = JwtUtils.generateToken(userEntry);

        CookieUtils.saveCookie(JwtUtils.getCookieName(), responseToken, JwtUtils.getJWT_TOKEN_VALIDITY(), response);

        return new ResponseEntity<>(new JwtToken(responseToken), HttpStatus.OK);
    }

    @PostMapping(path = "/register", consumes = "application/json")
    public ResponseEntity<String> registerRequest(@RequestBody @Valid User user) {
        if (userRepository.existsByUsername(user.getUsername()))
            return new ResponseEntity<>("Username already taken", HttpStatus.BAD_REQUEST);

        String encoded = passwordEncoder.encode(user.getPassword());
        user.setPassword(encoded);
        User createdUser = userRepository.save(user);
        roleHelper.addRoleTo(createdUser.getUserId(), Role.Roles.USER_ROLE);
//        Integer returnedUID = userRepository.returnValue(user.getUsername(), user.getPassword());
//        roleHelper.addRoleTo(returnedUID, Role.Roles.USER_ROLE);
        return new ResponseEntity<>("User created", HttpStatus.OK);
    }
}
