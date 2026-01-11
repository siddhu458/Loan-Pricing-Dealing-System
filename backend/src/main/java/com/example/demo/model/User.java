package com.example.demo.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String email;
    private String password;
    private Role role;
    private boolean active;

    private Instant createdAt;
    private Instant updatedAt;
}
