package com.contacts.demo.data.jdbcRepositories;

import com.contacts.demo.data.types.Person;
import com.contacts.demo.data.types.PhoneNumber;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class JdbcNameRepository implements IdRepository<Person>, CollapsingRepository<Person, PhoneNumber> {
    private JdbcTemplate jdbc;
    private Person mapRowToPerson(ResultSet result, int rowNum) throws SQLException {
//        return new Person(  null,
//                            result.getString("name"));
        return null;
    }

    private Person mapRowToPrivatePerson(ResultSet result, int rowNum) throws SQLException {
//        return new Person(  result.getInt("id"),
//                            result.getString("name"));
        return null;
    }

    private Pair<Person, PhoneNumber> mapRowToPair(ResultSet result, int rowNum) throws SQLException {
//        return new Pair<>(  new Person(result.getInt("person_id"), result.getString("name")),
//                            new PhoneNumber(null, result.getInt("person_id"), result.getString("number")));
        return null;
    }

    @Autowired
    public JdbcNameRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Iterable<Person> findAll() {
        return jdbc.query("SELECT name FROM public.persons", this::mapRowToPerson);
    }

    @Override
    public Iterable<Person> findByPid(Integer pid) {
        ArrayList<Person> result = new ArrayList<>();
        result.add(findById(pid));
        return result;
    }

    @Override
    public Iterable<Person> findSecureAll() {
        return jdbc.query("SELECT * FROM public.persons", this::mapRowToPrivatePerson);
    }

    @Override
    public Person findById(Integer id) {
        return jdbc.queryForObject("SELECT * FROM public.persons WHERE person_id=?", this::mapRowToPrivatePerson, id);
    }

    @Transactional
    @Override
    public Person save(Person name) {
        jdbc.update("INSERT INTO public.persons VALUES (?, ?)", name.getPersonId(), name.getName());
        return name;
    }

    @Transactional
    @Override
    public Person update(Integer id, Person updateEntity) {
        jdbc.update("UPDATE public.persons SET name=? WHERE person_id=?", updateEntity.getName(), id);
        return updateEntity;
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        jdbc.update("DELETE FROM public.persons WHERE person_id=?", id);
    }

    @Override
    public Iterable<Pair<Person, PhoneNumber>> mergeData() {
        return jdbc.query("SELECT name, number, person_id FROM persons JOIN phonenumbers ON persons.person_id=phonenumbers.person_id", this::mapRowToPair);
    }
}
