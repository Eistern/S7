package com.contacts.demo.data;

import com.contacts.demo.data.types.PhoneNumber;
import org.springframework.data.repository.CrudRepository;

public interface JpaNumberRepository extends CrudRepository<PhoneNumber, Integer> {
}
