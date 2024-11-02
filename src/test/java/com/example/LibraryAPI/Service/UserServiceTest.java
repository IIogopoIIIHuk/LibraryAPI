package com.example.LibraryAPI.Service;

import com.example.LibraryAPI.Repo.UserRepository;
import com.example.LibraryAPI.Role;
import com.example.LibraryAPI.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser() {
        User user = new User();
        user.setUsername("TestUser");
        user.setPassword("password");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.save(user);

        assertEquals("TestUser", savedUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCreateUser_Success() {
        User user = new User();
        user.setUsername("NewUser");

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.create(user);

        assertEquals("NewUser", createdUser.getUsername());
        verify(userRepository, times(1)).existsByUsername("NewUser");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCreateUser_UserAlreadyExists() {
        User user = new User();
        user.setUsername("ExistingUser");

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.create(user));
        verify(userRepository, times(1)).existsByUsername("ExistingUser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetByUsername_UserFound() {
        User user = new User();
        user.setUsername("ExistingUser");

        when(userRepository.findByUsername("ExistingUser")).thenReturn(Optional.of(user));

        User foundUser = userService.getByUsername("ExistingUser");

        assertEquals("ExistingUser", foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername("ExistingUser");
    }

    @Test
    void testGetByUsername_UserNotFound() {
        when(userRepository.findByUsername("NonExistentUser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getByUsername("NonExistentUser"));
        verify(userRepository, times(1)).findByUsername("NonExistentUser");
    }

    @Test
    void testGetCurrentUser() {
        User user = new User();
        user.setUsername("CurrentUser");

        // Установка SecurityContext и аутентификации
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        SecurityContextHolder.setContext(securityContext);
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("CurrentUser", null));

        when(userRepository.findByUsername("CurrentUser")).thenReturn(Optional.of(user));

        User currentUser = userService.getCurrentUser();

        assertEquals("CurrentUser", currentUser.getUsername());
        verify(userRepository, times(1)).findByUsername("CurrentUser");
    }

    @Test
    void testGetAdmin() {
        User user = new User();
        user.setUsername("CurrentUser");
        user.setRole(Role.ROLE_USER);

        // Установка SecurityContext и аутентификации
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        SecurityContextHolder.setContext(securityContext);
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("CurrentUser", null));

        when(userRepository.findByUsername("CurrentUser")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.getAdmin();

        assertEquals(Role.ROLE_ADMIN, user.getRole());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUserDetailsService() {
        // Мокирование UserRepository
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserDetailsService userDetailsService = userService.userDetailsService();

        assertNotNull(userDetailsService);
        assertDoesNotThrow(() -> userDetailsService.loadUserByUsername("testUser"));
    }
}
