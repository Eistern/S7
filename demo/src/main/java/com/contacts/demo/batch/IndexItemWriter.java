package com.contacts.demo.batch;

import com.contacts.demo.elasticsearch.PersonSearchEntity;
import com.contacts.demo.elasticsearch.SearchRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IndexItemWriter implements ItemWriter<PersonSearchEntity> {
    private final SearchRepository searchRepository;

    @Autowired
    public IndexItemWriter(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    @Override
    public void write(List<? extends PersonSearchEntity> list) {
        list.forEach(searchRepository::save);
    }
}
