package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.dto.RegisterRequestDTO;
import com.dpapie01.distributed_booking_system.dto.UserFilterDTO;
import com.dpapie01.distributed_booking_system.dto.UserRequestDTO;
import com.dpapie01.distributed_booking_system.dto.UserResponseDTO;
import com.dpapie01.distributed_booking_system.entity.Location;
import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.enums.Gender;
import com.dpapie01.distributed_booking_system.enums.Role;
import com.dpapie01.distributed_booking_system.mapper.UserMapper;
import com.dpapie01.distributed_booking_system.repository.CreditRepository;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
    private CreditRepository creditRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks private UserService userService;

    private RegisterRequestDTO registerRequest;
    private User savedUser;
    private User adminUser;
    private Location location;
    private UserResponseDTO userResponse;
    private UserResponseDTO adminUserResponse;

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

        adminUser = new User();
        adminUser.setId(2L);
        adminUser.setFirstName("Admin");
        adminUser.setLastName("User");
        adminUser.setUsername("adminuser");
        adminUser.setEmail("admin@example.com");
        adminUser.setRole(Role.ADMIN);
        adminUser.setActive(true);

        userResponse = new UserResponseDTO();
        userResponse.setId(1L);
        userResponse.setFirstName("Jon");
        userResponse.setLastName("Smith");
        userResponse.setUsername("jonsmith");
        userResponse.setEmail("jon@example.com");
        userResponse.setRole(Role.PLAYER);

        adminUserResponse = new UserResponseDTO();
        adminUserResponse.setId(2L);
        adminUserResponse.setFirstName("Admin");
        adminUserResponse.setLastName("User");
        adminUserResponse.setUsername("adminuser");
        adminUserResponse.setEmail("admin@example.com");
        adminUserResponse.setRole(Role.ADMIN);
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
        verify(creditRepository).save(argThat(credit ->
                credit.getUser() == savedUser && credit.getAmount().compareTo(BigDecimal.valueOf(100)) == 0));
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

    @Test
    void testGetAllUsers_NoFilters() {
        UserFilterDTO filter = new UserFilterDTO();

        when(userRepository.searchUsers(null, false, null)).thenReturn(List.of(savedUser, adminUser));
        when(userMapper.toResponseDTO(savedUser)).thenReturn(userResponse);
        when(userMapper.toResponseDTO(adminUser)).thenReturn(adminUserResponse);

        List<UserResponseDTO> result = userService.getAllUsers(filter);

        assertEquals(2, result.size());
        assertEquals("jonsmith", result.get(0).getUsername());
        assertEquals("adminuser", result.get(1).getUsername());
    }

    @Test
    void testGetAllUsers_WithSearchQuery() {
        UserFilterDTO filter = new UserFilterDTO();
        filter.setSearchQuery("jon");

        when(userRepository.searchUsers("jon", false, null)).thenReturn(List.of(savedUser));
        when(userMapper.toResponseDTO(savedUser)).thenReturn(userResponse);

        List<UserResponseDTO> result = userService.getAllUsers(filter);

        assertEquals(1, result.size());
        verify(userRepository).searchUsers("jon", false, null);
    }

    @Test
    void testGetAllUsers_ActiveOnly() {
        UserFilterDTO filter = new UserFilterDTO();
        filter.setActiveOnly(true);

        when(userRepository.searchUsers(null, true, null)).thenReturn(List.of(savedUser));
        when(userMapper.toResponseDTO(savedUser)).thenReturn(userResponse);

        List<UserResponseDTO> result = userService.getAllUsers(filter);

        assertEquals(1, result.size());
        assertEquals("jonsmith", result.getFirst().getUsername());
        verify(userRepository).searchUsers(null, true, null);
    }

    @Test
    void testGetAllUsers_FilteredByRole() {
        UserFilterDTO filter = new UserFilterDTO();
        filter.setRoles(List.of(Role.ADMIN));

        when(userRepository.searchUsers(null, false, List.of(Role.ADMIN))).thenReturn(List.of(adminUser));
        when(userMapper.toResponseDTO(adminUser)).thenReturn(adminUserResponse);

        List<UserResponseDTO> result = userService.getAllUsers(filter);

        assertEquals(1, result.size());
        assertEquals("adminuser", result.getFirst().getUsername());
        verify(userRepository).searchUsers(null, false, List.of(Role.ADMIN));
    }

    @Test
    void testUpdateUser_Valid() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setRole(Role.ADMIN);
        dto.setActive(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        when(userRepository.save(savedUser)).thenReturn(savedUser);
        when(userMapper.toResponseDTO(savedUser)).thenReturn(userResponse);

        userService.updateUser(dto, 1L, "admin@example.com");

        assertEquals(Role.ADMIN, savedUser.getRole());
        assertTrue(savedUser.getActive());
        verify(userRepository).save(savedUser);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setRole(Role.ADMIN);
        dto.setActive(true);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userService.updateUser(dto, 99L, "admin@example.com"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("User not found", ex.getReason());
    }

    @Test
    void testUpdateUser_CannotUpdateSelf() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setRole(Role.PLAYER);
        dto.setActive(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userService.updateUser(dto, 1L, "jon@example.com"));

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
        assertEquals("You cannot change your own role or active status", ex.getReason());
        verify(userRepository, never()).save(any());
    }
}
