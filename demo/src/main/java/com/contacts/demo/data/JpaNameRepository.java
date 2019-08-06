package com.contacts.demo.data;

import com.contacts.demo.data.types.Person;
import org.springframework.data.repository.CrudRepository;

public interface JpaNameRepository extends CrudRepository<Person, Integer> {

}
