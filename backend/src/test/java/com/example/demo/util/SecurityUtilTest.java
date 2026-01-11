package com.example.demo.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

class SecurityUtilTest {

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    // getCurrentUserEmail

    @Test
    void getCurrentUserEmail_whenAuthenticated_returnsEmail() {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        "user@mail.com",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        String email = SecurityUtil.getCurrentUserEmail();

        assertEquals("user@mail.com", email);
    }

    @Test
    void getCurrentUserEmail_whenNoAuthentication_returnsNull() {
        SecurityContextHolder.clearContext();

        String email = SecurityUtil.getCurrentUserEmail();

        assertNull(email);
    }

    // hasRole

    @Test
    void hasRole_whenUserHasRole_returnsTrue() {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        "admin@mail.com",
                        null,
                        List.of(
                                new SimpleGrantedAuthority("ROLE_ADMIN"),
                                new SimpleGrantedAuthority("ROLE_USER")
                        )
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        assertTrue(SecurityUtil.hasRole("ADMIN"));
        assertTrue(SecurityUtil.hasRole("USER"));
    }

    @Test
    void hasRole_whenUserDoesNotHaveRole_returnsFalse() {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        "user@mail.com",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        assertFalse(SecurityUtil.hasRole("ADMIN"));
    }

    @Test
    void hasRole_whenNoAuthentication_returnsFalse() {
        SecurityContextHolder.clearContext();

        assertFalse(SecurityUtil.hasRole("USER"));
    }
}

