package com.contacts.demo.kafka;

import com.contacts.demo.data.types.PhoneNumber;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
@AllArgsConstructor
public class NumberUpdateMessage {
    private final MessageTypes type;
    private final Integer uid;
    private final PhoneNumber number;
}
