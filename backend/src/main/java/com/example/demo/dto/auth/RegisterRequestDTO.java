package com.example.demo.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String role; // USER or ADMIN
}
