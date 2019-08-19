package com.contacts.demo.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchRepository extends ElasticsearchRepository<PersonSearchEntity, Integer> {
}
