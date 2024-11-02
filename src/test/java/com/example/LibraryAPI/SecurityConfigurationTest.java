package com.example.LibraryAPI;

import com.example.LibraryAPI.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SecurityConfigurationTest {

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private UserService userService;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @BeforeEach
    void setUp() {
        when(userService.userDetailsService()).thenReturn(mock(org.springframework.security.core.userdetails.UserDetailsService.class));
    }

    @Test
    void testPasswordEncoder() {
        PasswordEncoder encoder = securityConfiguration.passwordEncoder();
        assertNotNull(encoder, "PasswordEncoder должен быть инициализирован");
        assertTrue(encoder instanceof BCryptPasswordEncoder, "PasswordEncoder должен быть экземпляром BCryptPasswordEncoder");
    }

    @Test
    void testAuthenticationProvider() {
        AuthenticationProvider authProvider = securityConfiguration.authenticationProvider();
        assertNotNull(authProvider, "AuthenticationProvider должен быть инициализирован");
        assertTrue(authProvider instanceof DaoAuthenticationProvider, "AuthenticationProvider должен быть DaoAuthenticationProvider");
    }

    @Test
    void testAuthenticationManager() throws Exception {
        AuthenticationManager manager = securityConfiguration.authenticationManager(authenticationConfiguration);
        assertNotNull(manager, "AuthenticationManager должен быть инициализирован");
    }

    @Test
    void testSecurityFilterChainSetup() throws Exception {
        SecurityFilterChain securityFilterChain = securityConfiguration.securityFilterChain(mock(HttpSecurity.class));
        assertNotNull(securityFilterChain, "SecurityFilterChain должен быть инициализирован");
    }
}
