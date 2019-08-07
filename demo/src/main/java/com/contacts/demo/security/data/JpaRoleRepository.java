package com.contacts.demo.security.data;

import com.contacts.demo.security.data.types.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface JpaRoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByRoleName(String name);
}
