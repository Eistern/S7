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

    @NotNull
    @Size(min = 1, message = "List of phone numbers must include at least 1 number")
    private String phoneNumber;
}
