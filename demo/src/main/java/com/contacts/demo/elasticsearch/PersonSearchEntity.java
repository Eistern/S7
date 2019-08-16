package com.contacts.demo.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "contacts", type = "persons")
public class PersonSearchEntity {
    @Id
    private Integer personId;
    @Field(type = FieldType.Text, analyzer = "english")
    private String name;
}
