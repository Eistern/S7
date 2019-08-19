package com.contacts.demo.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "contacts", type = "persons")
public class PersonSearchEntity {
    @Id
    private Integer personId;

    @Field(type = FieldType.Text, analyzer = "english", searchAnalyzer = "simple")
    @NotEmpty
    @Size(max = 255, message = "Name can't contain more than 255 characters")
    private String name;
}
