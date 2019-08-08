package com.contacts.demo.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class JwtToken implements Serializable {
    private String token;
}
