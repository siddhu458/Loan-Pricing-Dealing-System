package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.config.JwtTokenProvider;
import com.example.demo.dto.auth.LoginRequestDTO;
import com.example.demo.dto.auth.LoginResponseDTO;
import com.example.demo.dto.auth.RegisterRequestDTO;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtProvider;

    @InjectMocks
    private AuthService authService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // REGISTER

    @Test
    void register_success() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setEmail("test@mail.com");
        dto.setPassword("password");
        dto.setRole("USER");

        when(userRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.empty());

        when(userRepository.save(any(User.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        assertDoesNotThrow(() -> authService.register(dto));

        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_userAlreadyExists_throwsException() {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setEmail("test@mail.com");

        when(userRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.of(new User()));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> authService.register(dto)
        );

        assertEquals("User already exists", ex.getMessage());
    }

    // LOGIN

    @Test
    void login_success() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("test@mail.com");
        dto.setPassword("password");

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setRole(Role.USER);

        when(userRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.of(user));

        when(jwtProvider.generateToken(
                user.getEmail(),
                user.getRole().name()))
                .thenReturn("jwt-token");

        LoginResponseDTO response = authService.login(dto);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
    }

    @Test
    void login_userNotFound_throwsException() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("missing@mail.com");

        when(userRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> authService.login(dto)
        );

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void login_invalidPassword_throwsException() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("test@mail.com");
        dto.setPassword("wrong");

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode("correct"));
        user.setRole(Role.USER);

        when(userRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.of(user));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> authService.login(dto)
        );

        assertEquals("Invalid credentials", ex.getMessage());
    }
}

