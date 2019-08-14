package com.example.demoreport.types;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
public class PhoneNumber implements Serializable {
    private final String phoneNumber;
    private final Integer phoneId;
    private final Integer personId;
}
