package com.contacts.demo.data.types;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Person {
    private Integer id = UUID.randomUUID().hashCode();

    @NotNull
    @Size(min = 1, message = "Contact name must consist at least 1 symbol")
    private String name;
}
