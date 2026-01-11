package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.auth.LoginRequestDTO;
import com.example.demo.dto.auth.LoginResponseDTO;
import com.example.demo.dto.auth.RegisterRequestDTO;
import com.example.demo.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final AuthService service;
	
	public AuthController(AuthService service) {
		this.service = service;
	}
	
	@PostMapping("/register")
	public String register(@Valid @RequestBody RegisterRequestDTO dto) {
	    service.register(dto);
	    return "User registered successfully";
	}

	
	@PostMapping("/login")
	public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO dto) {
		return service.login(dto);
	}
	
}
