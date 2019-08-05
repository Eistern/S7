package com.contacts.demo.data;

import com.contacts.demo.data.types.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcNumberRepository implements IdRepository<PhoneNumber> {
    private JdbcTemplate jdbc;
    private PhoneNumber mapRowToNumber(ResultSet result, int rowNum) throws SQLException {
        return new PhoneNumber( result.getInt("id"),
                                result.getInt("person_id"),
                                result.getString("number"));
    }

    @Autowired
    public JdbcNumberRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Iterable<PhoneNumber> findAll() {
        return jdbc.query("SELECT * FROM public.phonenumbers", this::mapRowToNumber);
    }

    @Override
    public PhoneNumber findOne(Integer id) {
        return jdbc.queryForObject("SELECT * FROM public.phonenumbers WHERE id=?", this::mapRowToNumber, id);
    }

    @Override
    public PhoneNumber save(PhoneNumber name) {
        jdbc.update("INSERT INTO public.phonenumbers VALUES (?, ?, ?)", name.getId(), name.getPerson_id(), name.getPhoneNumber());
        return name;
    }

    @Override
    public PhoneNumber update(Integer id, PhoneNumber updateEntity) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }
}
