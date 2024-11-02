package com.example.LibraryAPI.Service;

import com.example.LibraryAPI.Role;
import com.example.LibraryAPI.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails mockUserDetails;

    @Value("${token.signin.key}")
    private String jwtSigningKey = "testSigningKey1234567890testSigningKey1234567890";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtService, "jwtSigningKey", jwtSigningKey);
    }

    @Test
    void testGenerateToken() {

        User user = new User(1L, "testuser", "password", Role.ROLE_USER);


        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertTrue(token.startsWith("ey")); // Проверка, что токен начинается с ожидаемого префикса для JWT
    }

    @Test
    void testExtractUserName() {
        User user = new User(1L, "testuser", "password", Role.ROLE_USER);
        String token = jwtService.generateToken(user);

        String extractedUserName = jwtService.extractUserName(token);

        assertEquals("testuser", extractedUserName);
    }

    @Test
    void testIsTokenValid_ValidToken() {
        User user = new User(1L, "validuser", "password", Role.ROLE_USER);
        String token = jwtService.generateToken(user);

        boolean isValid = jwtService.isTokenValid(token, user);

        assertTrue(isValid);
    }

    @Test
    void testIsTokenValid_InvalidToken() {
        User user = new User(1L, "validuser", "password", Role.ROLE_USER);
        String token = jwtService.generateToken(user);

        User otherUser = new User(2L, "invaliduser", "password", Role.ROLE_USER);

        boolean isValid = jwtService.isTokenValid(token, otherUser);

        assertFalse(isValid);
    }

    @Test
    void testIsTokenExpired_NotExpiredToken() {
        User user = new User(1L, "testuser", "password", Role.ROLE_USER);
        String token = jwtService.generateToken(user);


        boolean isExpired = jwtService.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    void testExtractAllClaims() {
        User user = new User(1L, "testuser", "password", Role.ROLE_USER);
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("role", user.getRole());

        String token = jwtService.generateToken(claims, user);

        Claims extractedClaims = jwtService.extractAllClaims(token);

        assertEquals(user.getId(), extractedClaims.get("id", Long.class));
        assertEquals(user.getRole().name(), extractedClaims.get("role", String.class));
    }
}
