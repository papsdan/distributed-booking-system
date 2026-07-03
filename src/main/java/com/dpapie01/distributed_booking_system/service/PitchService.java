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

@RequiredArgsConstructor
@Service
public class PitchService {


    private final LocationRepository locationRepository;
    private final PitchMapper pitchMapper;
    private final PitchRepository pitchRepository;

    public List<PitchResponseDTO> getAllPitches() {
        return pitchRepository.findAll(Sort.by("id")).stream()
                .map(pitch -> pitchMapper.toResponseDTO(pitch))
                .toList();
    }

    public PitchResponseDTO getPitch(Long pitchId) {
        Pitch pitch = pitchRepository.findById(pitchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pitch not found"));

        return pitchMapper.toResponseDTO(pitch);
    }

    public PitchResponseDTO setActiveStatus(Long pitchId, boolean active) {
        Pitch pitch = pitchRepository.findById(pitchId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pitch not found"));

        pitch.setActive(active);

        return pitchMapper.toResponseDTO(pitchRepository.save(pitch));
    }

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
