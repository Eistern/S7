package com.contacts.demo.batch;

import com.contacts.demo.data.JpaNameRepository;
import com.contacts.demo.data.types.Person;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class IterablePersonReader implements ItemReader<Person> {
    private final JpaNameRepository nameRepository;
    private Integer repositoryCursor = 0;
    private Integer takenEntries = 0;
    private final Integer entriesNumber;

    @Autowired
    public IterablePersonReader(JpaNameRepository nameRepository) {
        this.nameRepository = nameRepository;
        this.entriesNumber = nameRepository.countAllByPersonIdGreaterThanEqual(0);
    }

    @Override
    public Person read() {
        if (takenEntries >= entriesNumber)
            return null;

        Optional<Person> resultedSearch;
        do {
            resultedSearch = nameRepository.findByPersonId(repositoryCursor);
            repositoryCursor++;
        } while (resultedSearch.isEmpty());

        takenEntries++;
        return resultedSearch.get();
    }
}
