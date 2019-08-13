package com.contacts.demo.kafka;

import com.contacts.demo.data.types.Person;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
public class PersonUpdateMessage implements Serializable {
    private final MessageTypes type;
    private final Integer uid;
    private final Person person;
}
