package com.example.LibraryAPI.Controller;

import com.example.LibraryAPI.*;
import com.example.LibraryAPI.Service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signUp_ShouldReturnJwtAuthenticationResponse() {
        SignUpRequest signUpRequest = new SignUpRequest("username", "password", "USER");
        JwtAuthenticationResponse expectedResponse = new JwtAuthenticationResponse("token");

        when(authenticationService.signUp(signUpRequest)).thenReturn(expectedResponse);

        JwtAuthenticationResponse actualResponse = authController.signUp(signUpRequest);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void signIn_ShouldReturnJwtAuthenticationResponse() {
        SignInRequest signInRequest = new SignInRequest("username", "password");
        JwtAuthenticationResponse expectedResponse = new JwtAuthenticationResponse("token");

        when(authenticationService.signIn(signInRequest)).thenReturn(expectedResponse);

        JwtAuthenticationResponse actualResponse = authController.signIn(signInRequest);

        assertEquals(expectedResponse, actualResponse);
    }
}
