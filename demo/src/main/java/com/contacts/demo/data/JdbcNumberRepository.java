package com.contacts.demo.data;

import com.contacts.demo.data.types.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcNumberRepository implements IdRepository<PhoneNumber> {
    private JdbcTemplate jdbc;
    private PhoneNumber mapRowToPrivateNumber(ResultSet result, int rowNum) throws SQLException {
        return new PhoneNumber( result.getInt("id"),
                                result.getInt("person_id"),
                                result.getString("number"));
    }

    private PhoneNumber mapRowToNumber(ResultSet result, int rowNum) throws SQLException {
        return new PhoneNumber( result.getInt("id"),
                                null,
                                result.getString("number"));
    }

    @Autowired
    public JdbcNumberRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Iterable<PhoneNumber> findAll() {
        return jdbc.query("SELECT id, number FROM public.phonenumbers", this::mapRowToNumber);
    }

    @Override
    public Iterable<PhoneNumber> findByPid(Integer pid) {
        return jdbc.query("SELECT * FROM public.phonenumbers WHERE person_id=?", this::mapRowToPrivateNumber, pid);
    }

    @Override
    public Iterable<PhoneNumber> findSecureAll() {
        return jdbc.query("SELECT * FROM public.phonenumbers", this::mapRowToPrivateNumber);
    }

    @Override
    public PhoneNumber findById(Integer id) {
        return jdbc.queryForObject("SELECT id, number FROM public.phonenumbers WHERE id=?", this::mapRowToNumber, id);
    }

    @Transactional
    @Override
    public PhoneNumber save(PhoneNumber number) {
        jdbc.update("INSERT INTO public.phonenumbers VALUES (?, ?, ?)", number.getId(), number.getPersonId(), number.getPhoneNumber());
        return number;
    }

    @Transactional
    @Override
    public PhoneNumber update(Integer id, PhoneNumber updateEntity) {
        jdbc.update("UPDATE public.phonenumbers SET person_id=?, number=? WHERE id=?",
                updateEntity.getPersonId(), updateEntity.getPhoneNumber(), id);
        return updateEntity;
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        jdbc.update("DELETE FROM public.phonenumbers WHERE id=?", id);
    }
}
