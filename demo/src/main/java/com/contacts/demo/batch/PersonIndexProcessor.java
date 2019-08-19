package com.contacts.demo.batch;

import com.contacts.demo.data.types.Person;
import com.contacts.demo.elasticsearch.PersonSearchEntity;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class PersonIndexProcessor implements ItemProcessor<Person, PersonSearchEntity> {

    @Override
    public PersonSearchEntity process(final Person person) {
        final Integer personId = person.getPersonId();
        final String name = person.getName();

        return new PersonSearchEntity(personId, name);
    }
}
