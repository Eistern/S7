package com.contacts.demo.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "contacts", type = "persons")
public class PersonSearchEntity {
    @Id
    private Integer id;
    private Integer personId;
    private String name;
}
