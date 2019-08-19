package com.contacts.demo.data;

import com.contacts.demo.data.types.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface JpaNameRepository extends CrudRepository<Person, Integer> {
    Integer countAllByPersonIdGreaterThanEqual(Integer personId);
    Optional<Person> findByPersonId(Integer personId);
    Iterable<Person> findAllByUid(Integer uid);
    boolean existsByPersonIdAndUid(Integer personId, Integer uid);
    Optional<Person> findByPersonIdAndUid(Integer personId, Integer uid);
}
