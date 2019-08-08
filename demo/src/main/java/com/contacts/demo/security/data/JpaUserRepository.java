package com.contacts.demo.security.data;

import com.contacts.demo.security.data.types.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface JpaUserRepository extends CrudRepository<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, name = "returnValue", value = "INSERT INTO auth (pwd, login) VALUES (:pwd, :login) RETURNING uid")
    int returnValue(@Param("login") String login, @Param("pwd") String pwd);
}
