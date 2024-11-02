package com.example.LibraryAPI;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationResponseTest {

    @Test
    void testJwtAuthenticationResponseConstructor() {
        String token = "testToken";

        JwtAuthenticationResponse response = new JwtAuthenticationResponse(token);

        assertEquals(token, response.getToken());
    }

    @Test
    void testJwtAuthenticationResponseBuilder() {
        String token = "testToken";

        JwtAuthenticationResponse response = JwtAuthenticationResponse.builder()
                .token(token)
                .build();

        assertEquals(token, response.getToken());
    }

    @Test
    void testJwtAuthenticationResponseSetToken() {
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        String token = "testToken";

        response.setToken(token);

        assertEquals(token, response.getToken());
    }

    @Test
    void testJwtAuthenticationResponseGetToken() {
        String token = "testToken";
        JwtAuthenticationResponse response = new JwtAuthenticationResponse(token);

        String retrievedToken = response.getToken();

        assertEquals(token, retrievedToken);
    }

    @Test
    void testJwtAuthenticationResponseDefaultConstructor() {
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();

        assertNull(response.getToken());
    }
}
