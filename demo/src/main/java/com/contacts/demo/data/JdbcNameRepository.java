package com.contacts.demo.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

//TODO implement method for SELECT name, number FROM person JOIN phonenumber ON person.id=phonenumber.person_id;

@Repository
public class JdbcNameRepository implements NameRepository {
    private static Integer id = 1;
    private JdbcTemplate jdbc;
    private String mapRowToString(ResultSet result, int rowNum) throws SQLException {
        return result.getString("name");
    }

    @Autowired
    public JdbcNameRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Iterable<String> findAll() {
        return jdbc.query("SELECT name FROM person", this::mapRowToString);
    }

    @Override
    public String findOne(String id) {
        return jdbc.queryForObject("SELECT name FROM person WHERE id=?", this::mapRowToString, id);
    }

    @Override
    public String save(String name) {
        jdbc.update("INSERT INTO person VALUES (?, ?)", (id++).toString(), name);
        return name;
    }
}
