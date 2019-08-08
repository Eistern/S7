package com.contacts.demo.security.data.types;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
@AllArgsConstructor
@Entity
@Table(schema = "public", name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private final Integer roleId;

    @NotEmpty
    @Column(name = "role_name")
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    private List<User> users;

    public enum Roles {
        USER_ROLE, ADMIN_ROLE;
    }
}
