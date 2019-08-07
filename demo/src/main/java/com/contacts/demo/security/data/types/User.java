package com.contacts.demo.security.data.types;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
@Entity
@Table(name = "auth", schema = "public")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "username")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    private final Integer userId;

    @NotEmpty
    @Column(name = "login", unique = true)
    private final String username;

    @NotEmpty
    @Column(name = "pwd")
    private String password;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable( name = "auth_roles",
                joinColumns = {@JoinColumn(name = "uid", referencedColumnName = "uid")},
                inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "role_id")})
    private List<Role> roles;

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("[userId:").append(userId).append(", ");
        buffer.append("username:").append(username).append(", ");
        buffer.append("password:").append(password).append(", ");
        buffer.append("roles:[");
        roles.forEach(role -> buffer.append(role.getRoleName()).append(", "));
        buffer.append("]]");
        return buffer.toString();
    }

    public List<String> getStringRoles() {
        List<String> result = new ArrayList<>();
        roles.forEach(role -> result.add(role.getRoleName()));
        return result;
    }
}
