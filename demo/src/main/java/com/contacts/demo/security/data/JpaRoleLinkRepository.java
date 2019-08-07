package com.contacts.demo.security.data;

import com.contacts.demo.security.data.types.UserRole;
import org.springframework.data.repository.CrudRepository;

public interface JpaRoleLinkRepository extends CrudRepository<UserRole, Integer> {
}
