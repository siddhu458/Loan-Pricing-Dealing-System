package com.example.demo.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.config.JwtTokenProvider;
import com.example.demo.dto.auth.LoginRequestDTO;
import com.example.demo.dto.auth.LoginResponseDTO;
import com.example.demo.dto.auth.RegisterRequestDTO;
import com.example.demo.repository.UserRepository;

@Service
public class AuthService {
	
	private final UserRepository userRepository;
	private final JwtTokenProvider jwtProvider;
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	public AuthService(UserRepository userRepository, JwtTokenProvider jwtProvider) {
		this.userRepository = userRepository;
		this.jwtProvider = jwtProvider;
	}
	
	public void register(RegisterRequestDTO dto) {

	    if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
	        throw new RuntimeException("User already exists");
	    }

	    var user = new com.example.demo.model.User();
	    user.setEmail(dto.getEmail());
	    user.setPassword(encoder.encode(dto.getPassword())); 
	    user.setRole(com.example.demo.model.Role.valueOf(dto.getRole()));
	    user.setActive(true);
	    user.setCreatedAt(java.time.Instant.now());

	    userRepository.save(user);
	}

	
	
	public LoginResponseDTO login(LoginRequestDTO dto) {
		var user = userRepository.findByEmail(dto.getEmail())
				.orElseThrow(() -> new RuntimeException("User not found"));
		
		if(!encoder.matches(dto.getPassword(), user.getPassword())) {
			throw new RuntimeException("Invalid credentials");
		}
		
		String token = jwtProvider.generateToken(
				user.getEmail(),
				user.getRole().name()
				);
		return new LoginResponseDTO(token);
	}
}
