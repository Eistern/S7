package com.contacts.demo.data;

import com.contacts.demo.data.types.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;

//TODO implement method for SELECT name, number FROM person JOIN phonenumber ON person.id=phonenumber.person_id;

@Repository
public class JdbcNameRepository implements IdRepository<Person> {
    private JdbcTemplate jdbc;
    private Person mapRowToPerson(ResultSet result, int rowNum) throws SQLException {
        return new Person(  result.getInt("id"),
                            result.getString("name"));
    }

    @Autowired
    public JdbcNameRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Iterable<Person> findAll() {
        return jdbc.query("SELECT * FROM public.person", this::mapRowToPerson);
    }

    @Override
    public Person findOne(Integer id) {
        return jdbc.queryForObject("SELECT * FROM person WHERE id=?", this::mapRowToPerson, id);
    }

    @Transactional
    @Override
    public Person save(Person name) {
        jdbc.update("INSERT INTO public.person VALUES (?, ?)", name.getId(), name.getName());
        return name;
    }

    @Transactional
    @Override
    public Person update(Integer id, Person updateEntity) {
        return null;
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {

    }
}
