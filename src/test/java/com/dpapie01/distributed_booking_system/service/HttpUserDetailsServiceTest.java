package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.enums.Role;
import com.dpapie01.distributed_booking_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HttpUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private HttpUserDetailsService httpUserDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("jon@example.com");
        user.setPassword("password123");
        user.setRole(Role.PLAYER);
        user.setActive(true);
    }

    @Test
    void testLoadUserByUsername_Found() {
        when(userRepository.findByEmail("jon@example.com")).thenReturn(Optional.of(user));

        UserDetails result = httpUserDetailsService.loadUserByUsername("jon@example.com");

        assertEquals("jon@example.com", result.getUsername());
        assertEquals("password123", result.getPassword());
        verify(userRepository).findByEmail("jon@example.com");
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        when(userRepository.findByEmail("jon@example.com")).thenReturn(Optional.empty());

        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class,
                () -> httpUserDetailsService.loadUserByUsername("jon@example.com"));

        assertEquals("User jon@example.com not found", ex.getMessage());
        verify(userRepository).findByEmail("jon@example.com");
    }

}
