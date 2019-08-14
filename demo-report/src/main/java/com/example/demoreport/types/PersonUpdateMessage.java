package com.example.demoreport.types;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
public class PersonUpdateMessage implements Serializable {
    private final Integer uid;
    private final MessageTypes type;
    private final Person person;
    private Date timestamp;

    public Integer getPersonId() {
        assert person != null;
        return person.getPersonId();
    }

    public String getName() {
        assert person != null;
        return person.getName();
    }

    public PersonUpdateMessage setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
