package com.contacts.demo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class NewContact {
    @NotNull
    @Size(min = 1, message = "Contact name must consist at least 1 symbol")
    private String name;

    @Pattern(regexp = "^[0-9][0-9]*$", message = "Phone number must include only numbers")
    private String phoneNumber;
}
