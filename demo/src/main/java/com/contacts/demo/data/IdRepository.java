package com.contacts.demo.data;

public interface IdRepository<T> {
    Iterable<T> findAll();
    T findOne(Integer id);
    T save(T name);
    T update(Integer id, T updateEntity);
    void deleteById(Integer id);
}
