package com.contacts.demo.kafka;

import com.example.demoreport.types.PhoneNumber;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE, force = true)
@Entity
@Table(schema = "logging", name = "number")
public class NumberUpdateMessage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "numberLogId", nullable = false, unique = true)
    private final Integer numberLogId;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private final MessageTypes type;
    @Column(name = "uid", nullable = false)
    private final Integer uid;

    private final PhoneNumber number;
    private Date timestamp;
    @Setter private Integer personId;
    @Setter private Integer phoneId;
    @Setter private String PhoneNumber;


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

    public NumberUpdateMessage setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
