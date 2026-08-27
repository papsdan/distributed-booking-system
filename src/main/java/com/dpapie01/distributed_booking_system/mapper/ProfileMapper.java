package com.dpapie01.distributed_booking_system.mapper;

import com.dpapie01.distributed_booking_system.dto.ProfileResponseDTO;
import com.dpapie01.distributed_booking_system.entity.Profile;
import org.springframework.stereotype.Component;

/**
 * This class maps Profile entities to ProfileResponseDTO for HTTP request responses.
 */
@Component
public class ProfileMapper {

    /**
     * Converts a profile and its preferred location details into a response DTO.
     * @param profile the profile
     * @return the mapped response DTO
     */
    public ProfileResponseDTO toResponseDTO(Profile profile) {
        ProfileResponseDTO dto = new ProfileResponseDTO();
        dto.setId(profile.getId());
        dto.setGender(profile.getGender());
        dto.setPreferredLocationId(profile.getPreferredLocation().getId());
        dto.setPreferredLocationCity(profile.getPreferredLocation().getCity());
        dto.setPreferredLocationArea(profile.getPreferredLocation().getArea());
        return dto;
    }
}
