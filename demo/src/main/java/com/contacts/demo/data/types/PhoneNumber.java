package com.contacts.demo.data.types;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PhoneNumber {
    private Integer id = UUID.randomUUID().hashCode();

    @NotNull
    private Integer personId;

    @NotNull
    @Pattern(regexp = "^[0-9]{7,11}$", message = "Phone number must include from 8 to 11 numbers")
    private String phoneNumber;
}
