package com.contacts.demo.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface SearchRepository extends ElasticsearchRepository<PersonSearchEntity, Integer> {
    List<PersonSearchEntity> findByName(String name);
}
