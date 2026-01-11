package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.demo.config.JwtAuthenticationFilter;
import com.example.demo.config.JwtTokenProvider;
import com.example.demo.dto.auth.LoginRequestDTO;
import com.example.demo.dto.auth.LoginResponseDTO;
import com.example.demo.dto.auth.RegisterRequestDTO;
import com.example.demo.services.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
	    controllers = AuthController.class,
	    excludeAutoConfiguration = {
	        org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
	    }
	)
	@AutoConfigureMockMvc(addFilters = false)
	class AuthControllerTest {


    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    // REGISTER
    @Test
    void register_success() throws Exception {

        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setEmail("user@mail.com");
        dto.setPassword("password");
        dto.setRole("USER");

        doNothing().when(authService).register(any(RegisterRequestDTO.class));

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    // LOGIN
    @Test
    void login_success() throws Exception {

        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("user@mail.com");
        dto.setPassword("password");

        LoginResponseDTO response =
                new LoginResponseDTO("jwt-token-123");

        when(authService.login(any(LoginRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-123"));
    }
}


