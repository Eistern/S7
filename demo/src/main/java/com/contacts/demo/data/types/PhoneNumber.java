package com.contacts.demo.data.types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PhoneNumber {
    private Integer id = UUID.randomUUID().hashCode();
    private Integer person_id = -1;

    @NotNull
    @Pattern(regexp = "^[0-9][0-9]{1-10}$", message = "Phone number must include only 10 numbers")
    private String phoneNumber;
}
