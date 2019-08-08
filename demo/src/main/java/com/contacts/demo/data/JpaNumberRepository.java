package com.contacts.demo.data;

import com.contacts.demo.data.types.PhoneNumber;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface JpaNumberRepository extends CrudRepository<PhoneNumber, Integer> {
    Iterable<PhoneNumber> findAllByOwnerUid(Integer owner_uid);
    Optional<PhoneNumber> findByPhoneIdAndOwnerUid(Integer phoneId, Integer owner_uid);
    boolean existsByPhoneIdAndOwnerUid(Integer phoneId, Integer owner_uid);
}
