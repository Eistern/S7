package com.contacts.demo.data.jdbcRepositories;

public interface IdRepository<T> {
    Iterable<T> findAll();
    Iterable<T> findByPid(Integer pid);
    Iterable<T> findSecureAll();
    T findById(Integer id);
    T save(T name);
    T update(Integer id, T updateEntity);
    void deleteById(Integer id);
}
