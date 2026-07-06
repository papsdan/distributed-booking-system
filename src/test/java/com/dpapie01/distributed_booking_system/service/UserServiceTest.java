package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.dto.RegisterRequestDTO;
import com.dpapie01.distributed_booking_system.dto.UserResponseDTO;
import com.dpapie01.distributed_booking_system.entity.Location;
import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.enums.Gender;
import com.dpapie01.distributed_booking_system.enums.Role;
import com.dpapie01.distributed_booking_system.mapper.UserMapper;
import com.dpapie01.distributed_booking_system.repository.LocationRepository;
import com.dpapie01.distributed_booking_system.repository.ProfileRepository;
import com.dpapie01.distributed_booking_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks private UserService userService;

    private RegisterRequestDTO registerRequest;
    private User savedUser;
    private Location location;
    private UserResponseDTO userResponse;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequestDTO();
        registerRequest.setFirstName("Jon");
        registerRequest.setLastName("Smith");
        registerRequest.setUsername("jonsmith");
        registerRequest.setEmail("jon@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setGender(Gender.MAN);
        registerRequest.setLocationId(1L);

        location = new Location();
        location.setId(1L);
        location.setCity("London");
        location.setArea("East");

        savedUser = new User();
        savedUser.setId(1L);
        savedUser.setFirstName("Jon");
        savedUser.setLastName("Smith");
        savedUser.setUsername("jonsmith");
        savedUser.setEmail("jon@example.com");
        savedUser.setRole(Role.PLAYER);
        savedUser.setActive(true);

        userResponse = new UserResponseDTO();
        userResponse.setId(1L);
        userResponse.setFirstName("Jon");
        userResponse.setLastName("Smith");
        userResponse.setUsername("jonsmith");
        userResponse.setEmail("jon@example.com");
        userResponse.setRole(Role.PLAYER);
    }
    

    @Test
    void testRegister_Valid() {
        when(userRepository.existsByUsername("jonsmith")).thenReturn(false);
        when(userRepository.existsByEmail("jon@example.com")).thenReturn(false);
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(passwordEncoder.encode("password123")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toResponseDTO(savedUser)).thenReturn(userResponse);

        UserResponseDTO result = userService.register(registerRequest);

        assertEquals(1L, result.getId());
        assertEquals("Jon", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("jonsmith", result.getUsername());
        assertEquals("jon@example.com", result.getEmail());
        assertEquals(Role.PLAYER, result.getRole());
        verify(userRepository).save(any(User.class));
        verify(profileRepository).save(any());
        verify(locationRepository).findById(1L);
    }

    @Test
    void testRegister_UsernameTaken() {
        when(userRepository.existsByUsername("jonsmith")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userService.register(registerRequest));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        assertEquals("Username already taken", ex.getReason());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegister_EmailTaken() {
        when(userRepository.existsByUsername("jonsmith")).thenReturn(false);
        when(userRepository.existsByEmail("jon@example.com")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userService.register(registerRequest));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        assertEquals("Email already registered", ex.getReason());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegister_LocationNotFound() {
        when(userRepository.existsByUsername("jonsmith")).thenReturn(false);
        when(userRepository.existsByEmail("jon@example.com")).thenReturn(false);
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userService.register(registerRequest));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Location not found", ex.getReason());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegister_EncodesPassword() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(passwordEncoder.encode("password123")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toResponseDTO(savedUser)).thenReturn(userResponse);

        userService.register(registerRequest);

        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(argThat(u -> "encoded".equals(u.getPassword())));
    }
}
