package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.dto.PitchRequestDTO;
import com.dpapie01.distributed_booking_system.dto.PitchResponseDTO;
import com.dpapie01.distributed_booking_system.entity.Location;
import com.dpapie01.distributed_booking_system.entity.Pitch;
import com.dpapie01.distributed_booking_system.mapper.PitchMapper;
import com.dpapie01.distributed_booking_system.repository.LocationRepository;
import com.dpapie01.distributed_booking_system.repository.PitchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
/**
 * This is a service class for viewing, creating, updating and managing pitch active status.
 */
@RequiredArgsConstructor
@Service
public class PitchService {


    private final LocationRepository locationRepository;
    private final PitchMapper pitchMapper;
    private final PitchRepository pitchRepository;
    /**
     * Gets all pitches sorted by ID and maps them to response DTOs.
     * @return a list of all pitch response DTOs
     */
    public List<PitchResponseDTO> getAllPitches() {
        return pitchRepository.findAll(Sort.by("id")).stream()
                .map(pitch -> pitchMapper.toResponseDTO(pitch))
                .toList();
    }
    /**
     * Get details for a pitch by its ID or throws NOT_FOUND if it does not exist.
     * @param pitchId the ID of the pitch
     * @return the pitch response DTO
     */
    public PitchResponseDTO getPitch(Long pitchId) {
        Pitch pitch = pitchRepository.findById(pitchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pitch not found"));

        return pitchMapper.toResponseDTO(pitch);
    }
    /**
     * Updates the active status of a pitch.
     * @param pitchId the ID of the pitch
     * @param active the new active status
     * @return the updated pitch response DTO
     */
    public PitchResponseDTO setActiveStatus(Long pitchId, boolean active) {
        Pitch pitch = pitchRepository.findById(pitchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pitch not found"));

        pitch.setActive(active);

        return pitchMapper.toResponseDTO(pitchRepository.save(pitch));
    }
    /**
     * Updates an existing pitch's name, location, capacity and active status.
     * Validates that the location exists and that capacity is an even number for balanced team allocation.
     * @param dto the pitch request DTO containing updated details
     * @param pitchId the ID of the pitch being updated
     * @param activeStatus the active status to set on the pitch
     * @return the updated pitch response DTO
     */
    public PitchResponseDTO updatePitch(PitchRequestDTO dto, Long pitchId, boolean activeStatus) {
        Pitch pitch = pitchRepository.findById(pitchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pitch not found"));

        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found"));

        if(dto.getCapacity() % 2 != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Capacity must be an even number so teams can be split evenly");
        }

        pitch.setName(dto.getName());
        pitch.setLocation(location);
        pitch.setCapacity(dto.getCapacity());
        pitch.setActive(activeStatus);

        return pitchMapper.toResponseDTO(pitchRepository.save(pitch));
    }
    /**
     * Creates a new pitch at the specified location.
     * Validates that the pitch name is unique for the location and capacity is an even number.
     * @param dto the pitch request DTO containing creation details
     * @return the created pitch response DTO
     */
    public PitchResponseDTO createPitch(PitchRequestDTO dto) {
        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found"));

        if(pitchRepository.existsByNameAndLocation(dto.getName(),location)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pitch already exists. There is the same pitch name in that location");
        }
        if(dto.getCapacity() % 2 != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Capacity must be an even number so teams can be split evenly");
        }

        Pitch pitch = new Pitch();
        pitch.setName(dto.getName());
        pitch.setLocation(location);
        pitch.setCapacity(dto.getCapacity());

        return pitchMapper.toResponseDTO(pitchRepository.save(pitch));
    }


}
