package com.dpapie01.distributed_booking_system.mapper;

import com.dpapie01.distributed_booking_system.dto.ProfileResponseDTO;
import com.dpapie01.distributed_booking_system.entity.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public ProfileResponseDTO toResponseDTO(Profile profile) {
        ProfileResponseDTO dto = new ProfileResponseDTO();
        dto.setId(profile.getId());
        dto.setGender(profile.getGender());
        dto.setPreferredLocationCity(profile.getPreferredLocation().getCity());
        dto.setPreferredLocationArea(profile.getPreferredLocation().getArea());
        return dto;
    }
}
