package com.contacts.demo.kafka;

import com.example.demoreport.types.Person;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
@Entity
@Table(name = "person", schema = "logging")
public class PersonUpdateMessage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private final Integer personLogId;

    @Column(name = "uid", nullable = false)
    private final Integer uid;
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private final MessageTypes type;

    @Setter private String name;
    @Setter private Integer personId;

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
