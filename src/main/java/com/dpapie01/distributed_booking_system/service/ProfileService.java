package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.dto.ProfileResponseDTO;
import com.dpapie01.distributed_booking_system.dto.UpdateProfileRequestDTO;
import com.dpapie01.distributed_booking_system.entity.Location;
import com.dpapie01.distributed_booking_system.entity.Profile;
import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.mapper.ProfileMapper;
import com.dpapie01.distributed_booking_system.repository.LocationRepository;
import com.dpapie01.distributed_booking_system.repository.ProfileRepository;
import com.dpapie01.distributed_booking_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
/**
 * This is a service class for viewing and updating user profiles and location/gender preferences.
 */
@RequiredArgsConstructor
@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final ProfileMapper profileMapper;
    /**
     * Gets the user's profile by getting the User by email and their Profile record.
     * Maps the entity to a ProfileResponseDTO.
     * @param email the email of the user
     * @return the profile response DTO
     */
    public ProfileResponseDTO getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));

        return profileMapper.toResponseDTO(profile);
    }
    /**
     * Updates a user's preferredLocation and gender on their Profile.
     * Validates that the user, profile and location exist before saving the updated profile.
     * @param dto the update profile request DTO containing the new location ID and gender
     * @param email the email of the user updating their profile
     * @return the updated profile response DTO
     */
    public ProfileResponseDTO updateProfile(UpdateProfileRequestDTO dto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));

        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found"));

        profile.setPreferredLocation(location);
        profile.setGender(dto.getGender());

        return profileMapper.toResponseDTO(profileRepository.save(profile));
    }
}
