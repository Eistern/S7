package com.contacts.demo.data;

import com.contacts.demo.data.types.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

//TODO implement method for SELECT name, number FROM person JOIN phonenumber ON person.id=phonenumber.person_id;

@Repository
public class JdbcNameRepository implements IdRepository<Person> {
    private JdbcTemplate jdbc;
    private Person mapRowToPerson(ResultSet result, int rowNum) throws SQLException {
        return new Person(  null,
                            result.getString("name"));
    }

    private Person mapRowToPrivatePerson(ResultSet result, int rowNum) throws SQLException {
        return new Person(  result.getInt("id"),
                            result.getString("name"));
    }

    @Autowired
    public JdbcNameRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Iterable<Person> findAll() {
        return jdbc.query("SELECT name FROM public.person", this::mapRowToPerson);
    }

    @Override
    public Iterable<Person> findByPid(Integer pid) {
        ArrayList<Person> result = new ArrayList<>();
        result.add(findById(pid));
        return result;
    }

    @Override
    public Iterable<Person> findSecureAll() {
        return jdbc.query("SELECT * FROM public.person", this::mapRowToPrivatePerson);
    }

    @Override
    public Person findById(Integer id) {
        return jdbc.queryForObject("SELECT * FROM public.person WHERE id=?", this::mapRowToPrivatePerson, id);
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
        jdbc.update("UPDATE public.person SET name=? WHERE id=?", updateEntity.getName(), id);
        return updateEntity;
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        jdbc.update("DELETE FROM public.person WHERE id=?", id);
    }
}
