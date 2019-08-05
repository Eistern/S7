package com.contacts.demo;

import com.contacts.demo.data.IdRepository;
import com.contacts.demo.data.JdbcNameRepository;
import com.contacts.demo.data.types.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JdbcNameRepository.class)
public class NameRepositoryTests {
    @Autowired
    private IdRepository<Person> nameRepository;

    @Test
    public void testFind() {
        Person result = nameRepository.findById(0);
        assert result != null;
        assert result.getId().equals(0);
        assert result.getName().endsWith("Tester");
    }
}
