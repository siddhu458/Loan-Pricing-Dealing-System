package com.example.demo.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "deals")
public class Deal {

    @Id
    private String id;

    private String name;
    private String stage;
    private Double amount;
}

