package com.dpapie01.distributed_booking_system.mapper;

import com.dpapie01.distributed_booking_system.dto.PitchResponseDTO;
import com.dpapie01.distributed_booking_system.entity.Pitch;
import org.springframework.stereotype.Component;

@Component
public class PitchMapper {

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
