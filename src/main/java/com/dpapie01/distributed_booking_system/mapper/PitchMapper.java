package com.dpapie01.distributed_booking_system.mapper;

import com.dpapie01.distributed_booking_system.dto.PitchResponseDTO;
import com.dpapie01.distributed_booking_system.entity.Pitch;
import org.springframework.stereotype.Component;

/**
 * This class maps Pitch entities to PitchResponseDTO for HTTP request responses.
 */
@Component
public class PitchMapper {

    /**
     * Converts a pitch and its location details into a response DTO.
     * @param pitch the pitch
     * @return the mapped response DTO
     */
    public PitchResponseDTO toResponseDTO(Pitch pitch) {
        PitchResponseDTO dto = new PitchResponseDTO();
        dto.setId(pitch.getId());
        dto.setName(pitch.getName());
        dto.setLocationId(pitch.getLocation().getId());
        dto.setLocationCity(pitch.getLocation().getCity());
        dto.setLocationArea(pitch.getLocation().getArea());
        dto.setCapacity(pitch.getCapacity());
        dto.setActive(pitch.getActive());
        return dto;
    }
}
