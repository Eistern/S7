package com.example.democlient;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
public class PhoneNumber implements Serializable {
    private final Integer phoneId;
    private final Integer personId;
    private final String phoneNumber;
    private final String owner;
}
