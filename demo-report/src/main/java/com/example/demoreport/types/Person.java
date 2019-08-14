package com.example.demoreport.types;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
public class Person implements Serializable {
    private final String name;
    private final Integer uid;
    private final Integer personId;
}
