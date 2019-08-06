package com.contacts.demo.data.types;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
@Entity
@Table(name = "phonenumbers", schema = "public")
public class PhoneNumber implements Serializable {
    public PhoneNumber(Integer personId, String phoneNumber) {
        this.id = null;
        this.personId = personId;
        this.phoneNumber = phoneNumber;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phone_id")
    private final Integer id;

    @NotNull
    @Column(name = "person_id")
    private Integer personId;

    @NotNull
    @Pattern(regexp = "^[0-9]{7,11}$", message = "Phone number must include from 8 to 11 numbers")
    @Column(name = "number", length = 11)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false, insertable = false, updatable = false)
    private Person owner;
}
