package com.example.LibraryAPI.Service;

import com.example.LibraryAPI.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignUp() {
        SignUpRequest request = new SignUpRequest("newUser", "password123", "ROLE_USER");
        String encodedPassword = "encodedPassword123";
        String generatedToken = "generatedJwtToken";

        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);
        when(jwtService.generateToken(any(User.class))).thenReturn(generatedToken);

        JwtAuthenticationResponse response = authenticationService.signUp(request);

        assertEquals(generatedToken, response.getToken());
        verify(userService, times(1)).create(any(User.class));
        verify(jwtService, times(1)).generateToken(any(User.class));
    }

    @Test
    void testSignIn() {
        SignInRequest request = new SignInRequest("existingUser", "password123");
        String generatedToken = "generatedJwtToken";

        UserDetailsService mockUserDetailsService = mock(UserDetailsService.class);
        when(userService.userDetailsService()).thenReturn(mockUserDetailsService);

        User user = User.builder()
                .username(request.getUsername())
                .password("encodedPassword")
                .role(Role.ROLE_USER)
                .build();
        when(mockUserDetailsService.loadUserByUsername(request.getUsername())).thenReturn(user);

        when(jwtService.generateToken(user)).thenReturn(generatedToken);

        JwtAuthenticationResponse response = authenticationService.signIn(request);

        assertEquals(generatedToken, response.getToken());
        verify(authenticationManager, times(1)).authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        verify(jwtService, times(1)).generateToken(user);
    }

}
