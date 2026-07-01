package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.dto.ProfileResponseDTO;
import com.dpapie01.distributed_booking_system.dto.UpdateProfileRequestDTO;
import com.dpapie01.distributed_booking_system.entity.Location;
import com.dpapie01.distributed_booking_system.entity.Profile;
import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.enums.Gender;
import com.dpapie01.distributed_booking_system.enums.Role;
import com.dpapie01.distributed_booking_system.mapper.ProfileMapper;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProfileMapper profileMapper;
    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private ProfileService profileService;

    private User user;
    private Location location;
    private Location updatedLocation;
    private Profile profile;
    private ProfileResponseDTO profileResponse;
    private UpdateProfileRequestDTO updateProfileRequest;
    private ProfileResponseDTO updatedProfileResponse;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("Jon");
        user.setLastName("Smith");
        user.setUsername("jonsmith");
        user.setEmail("jon@example.com");
        user.setRole(Role.PLAYER);
        user.setActive(true);

        location = new Location();
        location.setId(1L);
        location.setCity("London");
        location.setArea("Finsbury Park");

        profile = new Profile();
        profile.setId(1L);
        profile.setUser(user);
        profile.setGender(Gender.MALE);
        profile.setPreferredLocation(location);

        profileResponse = new ProfileResponseDTO();
        profileResponse.setId(1L);
        profileResponse.setGender(Gender.MALE);
        profileResponse.setPreferredLocationCity("London");
        profileResponse.setPreferredLocationArea("Finsbury Park");

        updatedLocation = new Location();
        updatedLocation.setId(2L);
        updatedLocation.setCity("Manchester");
        updatedLocation.setArea("Salford");

        updateProfileRequest = new UpdateProfileRequestDTO();
        updateProfileRequest.setLocationId(2L);
        updateProfileRequest.setGender(Gender.FEMALE);

        updatedProfileResponse = new ProfileResponseDTO();
        updatedProfileResponse.setId(1L);
        updatedProfileResponse.setGender(Gender.FEMALE);
        updatedProfileResponse.setPreferredLocationCity("Manchester");
        updatedProfileResponse.setPreferredLocationArea("Salford");
    }

    @Test
    void testGetProfile_Found() {
        when(userRepository.findByEmail("jon@example.com")).thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user)).thenReturn(Optional.of(profile));
        when(profileMapper.toResponseDTO(profile)).thenReturn(profileResponse);

        ProfileResponseDTO result = profileService.getProfile("jon@example.com");

        assertEquals(1L, result.getId());
        assertEquals(Gender.MALE, result.getGender());
        assertEquals("London", result.getPreferredLocationCity());
        assertEquals("Finsbury Park", result.getPreferredLocationArea());
        verify(userRepository).findByEmail("jon@example.com");
        verify(profileRepository).findByUser(user);
        verify(profileMapper).toResponseDTO(profile);
    }

    @Test
    void testGetProfile_UserNotFound() {
        when(userRepository.findByEmail("jon@example.com")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> profileService.getProfile("jon@example.com"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("User not found", ex.getReason());
        verify(userRepository).findByEmail("jon@example.com");
    }

    @Test
    void testGetProfile_ProfileNotFound() {
        when(userRepository.findByEmail("jon@example.com")).thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> profileService.getProfile("jon@example.com"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Profile not found", ex.getReason());
        verify(userRepository).findByEmail("jon@example.com");
        verify(profileRepository).findByUser(user);
    }

    @Test
    void testUpdateProfile_UserNotFound() {
        when(userRepository.findByEmail("jon@example.com")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> profileService.updateProfile(updateProfileRequest,"jon@example.com"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("User not found", ex.getReason());
        verify(userRepository).findByEmail("jon@example.com");
    }

    @Test
    void testUpdateProfile_ProfileNotFound() {
        when(userRepository.findByEmail("jon@example.com")).thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> profileService.updateProfile(updateProfileRequest,"jon@example.com"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Profile not found", ex.getReason());
        verify(userRepository).findByEmail("jon@example.com");
        verify(profileRepository).findByUser(user);
    }

    @Test
    void testUpdateProfile_LocationNotFound() {
        when(userRepository.findByEmail("jon@example.com")).thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user)).thenReturn(Optional.of(profile));
        when(locationRepository.findById(2L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> profileService.updateProfile(updateProfileRequest,"jon@example.com"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Location not found", ex.getReason());
        verify(userRepository).findByEmail("jon@example.com");
        verify(profileRepository).findByUser(user);
        verify(locationRepository).findById(2L);
    }

    @Test
    void testUpdateProfile_Valid() {
        when(userRepository.findByEmail("jon@example.com")).thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user)).thenReturn(Optional.of(profile));
        when(locationRepository.findById(2L)).thenReturn(Optional.of(updatedLocation));
        when(profileMapper.toResponseDTO(profile)).thenReturn(updatedProfileResponse);
        when(profileRepository.save(profile)).thenReturn(profile);

        ProfileResponseDTO result = profileService.updateProfile(updateProfileRequest,"jon@example.com");

        assertEquals(1L, result.getId());
        assertEquals(Gender.FEMALE, result.getGender());
        assertEquals("Manchester", result.getPreferredLocationCity());
        assertEquals("Salford", result.getPreferredLocationArea());

        verify(userRepository).findByEmail("jon@example.com");
        verify(profileRepository).findByUser(user);
        verify(locationRepository).findById(2L);
        verify(profileMapper).toResponseDTO(profile);
        verify(profileRepository).save(profile);
    }
}