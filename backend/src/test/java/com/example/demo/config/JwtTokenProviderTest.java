package com.example.demo.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;

    @BeforeEach
    void setup() {
        tokenProvider = new JwtTokenProvider();
    }

    // GENERATE TOKEN 

    @Test
    void generateToken_success() {
        String token = tokenProvider.generateToken(
                "user@mail.com",
                "USER"
        );

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    // VALIDATE TOKEN

    @Test
    void validateToken_success_returnsClaims() {
        String token = tokenProvider.generateToken(
                "user@mail.com",
                "ADMIN"
        );

        Claims claims = tokenProvider.validateToken(token);

        assertNotNull(claims);
        assertEquals("user@mail.com", claims.getSubject());
        assertEquals("ROLE_ADMIN", claims.get("role", String.class));
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    // INVALID TOKEN

    @Test
    void validateToken_invalidToken_throwsException() {
        String invalidToken = "invalid.jwt.token";

        assertThrows(JwtException.class, () ->
                tokenProvider.validateToken(invalidToken)
        );
    }

}
