package com.contacts.demo;

import lombok.Data;
import lombok.Generated;

import javax.validation.constraints.Pattern;

@Data
public class PhoneNumber {
    @Generated
    private Integer id;

    @Pattern(regexp = "^[0-9][0-9]{10}$", message = "Phone number must include only 10 numbers")
    private String phoneNumber;
}
