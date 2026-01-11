package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repo;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    // GET BY EMAIL

    @Test
    void getByEmail_success() {
        User user = new User();
        user.setEmail("test@mail.com");

        when(repo.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));

        User result = userService.getByEmail("test@mail.com");

        assertEquals("test@mail.com", result.getEmail());
    }

    @Test
    void getByEmail_notFound_throwsException() {
        when(repo.findByEmail("missing@mail.com"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> userService.getByEmail("missing@mail.com")
        );

        assertEquals("User not found", ex.getMessage());
    }

    // FIND ALL

    @Test
    void findAll_success() {
        when(repo.findAll())
                .thenReturn(List.of(new User(), new User()));

        List<User> users = userService.findAll();

        assertEquals(2, users.size());
    }

    // CREATE

    @Test
    void create_success() {
        User user = new User();
        user.setPassword("plainPassword");

        when(encoder.encode("plainPassword"))
                .thenReturn("encodedPassword");

        when(repo.save(any(User.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        User saved = userService.create(user);

        assertEquals("encodedPassword", saved.getPassword());
        assertTrue(saved.isActive());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());

        verify(repo).save(user);
    }

    // UPDATE STATUS

    @Test
    void updateStatus_success() {
        User user = new User();
        user.setActive(true);

        when(repo.findById("1"))
                .thenReturn(Optional.of(user));

        when(repo.save(any(User.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        User updated = userService.updateStatus("1", false);

        assertFalse(updated.isActive());
        assertNotNull(updated.getUpdatedAt());
    }

    @Test
    void updateStatus_userNotFound_throwsException() {
        when(repo.findById("1"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> userService.updateStatus("1", true)
        );

        assertEquals("User not found", ex.getMessage());
    }
}

