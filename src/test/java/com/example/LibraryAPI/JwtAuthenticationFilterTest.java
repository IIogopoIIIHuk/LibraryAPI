package com.example.LibraryAPI;

import com.example.LibraryAPI.Service.JwtService;
import com.example.LibraryAPI.Service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetailsService = mock(UserDetailsService.class);
        when(userService.userDetailsService()).thenReturn(userDetailsService);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_WhenTokenIsMissing_ShouldProceedWithoutAuthentication() throws ServletException, IOException {
        when(request.getHeader(JwtAuthenticationFilter.HEADER_NAME)).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_WhenTokenIsInvalid_ShouldProceedWithoutAuthentication() throws ServletException, IOException {
        String invalidToken = "invalidToken";
        String authHeader = JwtAuthenticationFilter.BEARER_PREFIX + invalidToken;

        when(request.getHeader(JwtAuthenticationFilter.HEADER_NAME)).thenReturn(authHeader);
        when(jwtService.extractUserName(invalidToken)).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_WhenTokenIsValid_ShouldAuthenticateUser() throws ServletException, IOException {
        String validToken = "validToken";
        String username = "testUser";
        String authHeader = JwtAuthenticationFilter.BEARER_PREFIX + validToken;
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getHeader(JwtAuthenticationFilter.HEADER_NAME)).thenReturn(authHeader);
        when(jwtService.extractUserName(validToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(validToken, userDetails)).thenReturn(true);
        when(userDetails.getUsername()).thenReturn(username); // Устанавливаем имя пользователя

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Проверяем что аутентификация была установлена
        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getName());
        assertEquals(0, SecurityContextHolder.getContext().getAuthentication().getAuthorities().size());

        // Проверяем что фильтр был вызван
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WhenTokenIsValid_ShouldSetAuthenticationDetails() throws ServletException, IOException {
        String validToken = "validToken";
        String username = "testUser";
        String authHeader = JwtAuthenticationFilter.BEARER_PREFIX + validToken;
        UserDetails userDetails = mock(UserDetails.class);

        // Настройка авторизации для userDetails
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList()); // Возвращает пустой список авторизаций
        when(userDetails.getUsername()).thenReturn(username); // Устанавливает имя пользователя

        when(request.getHeader(JwtAuthenticationFilter.HEADER_NAME)).thenReturn(authHeader);
        when(jwtService.extractUserName(validToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(validToken, userDetails)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Проверка что аутентификация была установлена
        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getName());

        // Проверяет что фильтр был вызван
        verify(filterChain).doFilter(request, response);
    }
}
