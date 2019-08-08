package com.contacts.demo.data.types;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
@Entity
@Table(name = "persons", schema = "public")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Person implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private final Integer personId;

    @NotNull
    @Size(min = 1, message = "Contact name must consist from 1 to 255 symbols", max = 255)
    private String name;

    @Column(name = "uid")
    @JoinColumn(name = "uid", referencedColumnName = "uid", table = "auth")
    private Integer uid;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private List<PhoneNumber> phoneNumbers;
}
