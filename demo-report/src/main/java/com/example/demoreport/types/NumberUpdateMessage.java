package com.example.demoreport.types;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
public class NumberUpdateMessage implements Serializable {
    private final MessageTypes type;
    private final Integer uid;
    private final PhoneNumber number;

    public Integer getPhoneId() {
        assert number != null;
        return number.getPhoneId();
    }

    public String getPhoneNumber() {
        assert number != null;
        return number.getPhoneNumber();
    }

    public Integer getPersonId() {
        assert number != null;
        return number.getPersonId();
    }
}
