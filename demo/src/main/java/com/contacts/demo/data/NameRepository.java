package com.contacts.demo.data;

public interface NameRepository {
    Iterable<String> findAll();
    String findOne(String id);
    String save(String name);
}
