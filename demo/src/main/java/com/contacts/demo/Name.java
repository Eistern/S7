package com.contacts.demo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class Name {
    private Integer id;

    @NotNull
    @Size(min = 1, message = "Contact name must consist at least 1 symbol")
    private String name;
}
