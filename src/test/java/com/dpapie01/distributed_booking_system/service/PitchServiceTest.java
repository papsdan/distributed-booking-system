package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.dto.PitchRequestDTO;
import com.dpapie01.distributed_booking_system.dto.PitchResponseDTO;
import com.dpapie01.distributed_booking_system.entity.Location;
import com.dpapie01.distributed_booking_system.entity.Pitch;
import com.dpapie01.distributed_booking_system.mapper.PitchMapper;
import com.dpapie01.distributed_booking_system.repository.LocationRepository;
import com.dpapie01.distributed_booking_system.repository.PitchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PitchServiceTest {

    @Mock
    private PitchRepository pitchRepository;
    @Mock
    private PitchMapper pitchMapper;
    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private PitchService pitchService;

    private Location location;
    private Pitch pitch;
    private PitchResponseDTO pitchResponse;
    private PitchRequestDTO pitchRequest;
    private PitchRequestDTO updatePitchRequest;
    private PitchResponseDTO updatePitchResponse;

    @BeforeEach
    void setUp() {

        location = new Location();
        location.setId(1L);
        location.setCity("London");
        location.setArea("Finsbury Park");

        pitch = new Pitch();
        pitch.setId(1L);
        pitch.setName("Pitch 1");
        pitch.setLocation(location);
        pitch.setCapacity(22);

        pitchResponse = new PitchResponseDTO();
        pitchResponse.setId(1L);
        pitchResponse.setName("Pitch 1");
        pitchResponse.setCapacity(22);
        pitchResponse.setLocationCity("London");
        pitchResponse.setLocationArea("Finsbury Park");
        pitchResponse.setLocationId(1L);
        pitchResponse.setActive(true);

        pitchRequest = new PitchRequestDTO();
        pitchRequest.setName("Pitch 1");
        pitchRequest.setLocationId(1L);
        pitchRequest.setCapacity(22);

        updatePitchRequest = new PitchRequestDTO();
        updatePitchRequest.setName("Pitch 1 Updated");
        updatePitchRequest.setLocationId(1L);
        updatePitchRequest.setCapacity(20);

        updatePitchResponse = new PitchResponseDTO();
        updatePitchResponse.setId(1L);
        updatePitchResponse.setName("Pitch 1 Updated");
        updatePitchResponse.setCapacity(20);
        updatePitchResponse.setLocationCity("London");
        updatePitchResponse.setLocationArea("Finsbury Park");
        updatePitchResponse.setLocationId(1L);
        updatePitchResponse.setActive(true);

    }

    @Test
    void testGetPitch_Found() {
        when(pitchRepository.findById(1L)).thenReturn(Optional.of(pitch));
        when(pitchMapper.toResponseDTO(pitch)).thenReturn(pitchResponse);

        PitchResponseDTO result = pitchService.getPitch(1L);

        assertEquals(1L, result.getId());
        assertEquals("Pitch 1", result.getName());
        assertEquals("London", result.getLocationCity());
        assertEquals("Finsbury Park", result.getLocationArea());
        assertEquals(22, result.getCapacity());
        assertEquals(1L, result.getLocationId());
        assertEquals(true, result.getActive());
        verify(pitchRepository).findById(1L);
    }

    @Test
    void testGetPitch_NotFound() {
        when(pitchRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> pitchService.getPitch(1L));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Pitch not found", ex.getReason());
        verify(pitchRepository).findById(1L);
    }

    @Test
    void testCreatePitch_LocationNotFound() {
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> pitchService.createPitch(pitchRequest));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Location not found", ex.getReason());
        verify(locationRepository).findById(1L);
    }

    @Test
    void testCreatePitch_DuplicateNameAndLocation() {
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(pitchRepository.existsByNameAndLocation("Pitch 1", location)).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> pitchService.createPitch(pitchRequest));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Pitch already exists. There is the same pitch name in that location", ex.getReason());
        verify(locationRepository).findById(1L);
        verify(pitchRepository).existsByNameAndLocation("Pitch 1", location);
    }

    @Test
    void testCreatePitch_OddCapacity() {
        pitchRequest.setCapacity(15);
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(pitchRepository.existsByNameAndLocation("Pitch 1", location)).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> pitchService.createPitch(pitchRequest));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Capacity must be an even number so teams can be split evenly", ex.getReason());
        verify(locationRepository).findById(1L);
        verify(pitchRepository).existsByNameAndLocation("Pitch 1", location);
    }

    @Test
    void testCreatePitch_Valid() {
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(pitchRepository.existsByNameAndLocation("Pitch 1", location)).thenReturn(false);
        when(pitchRepository.save(any(Pitch.class))).thenReturn(pitch);
        when(pitchMapper.toResponseDTO(pitch)).thenReturn(pitchResponse);

        PitchResponseDTO result = pitchService.createPitch(pitchRequest);

        assertEquals(1L, result.getId());
        assertEquals("Pitch 1", result.getName());
        assertEquals("London", result.getLocationCity());
        assertEquals("Finsbury Park", result.getLocationArea());
        assertEquals(1L, result.getLocationId());
        assertEquals(22, result.getCapacity());
        verify(locationRepository).findById(1L);
        verify(pitchRepository).existsByNameAndLocation("Pitch 1", location);
        verify(pitchMapper).toResponseDTO(pitch);
        verify(pitchRepository).save(any(Pitch.class));
    }

    @Test
    void testUpdatePitch_NotFound() {
        when(pitchRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> pitchService.updatePitch(pitchRequest, 1L, true));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Pitch not found", ex.getReason());
        verify(pitchRepository).findById(1L);
    }

    @Test
    void testUpdatePitch_LocationNotFound() {
        when(pitchRepository.findById(1L)).thenReturn(Optional.of(pitch));
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> pitchService.updatePitch(pitchRequest, 1L, true));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Location not found", ex.getReason());
    }

    @Test
    void testUpdatePitch_OddCapacity() {
        pitchRequest.setCapacity(15);
        when(pitchRepository.findById(1L)).thenReturn(Optional.of(pitch));
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> pitchService.updatePitch(pitchRequest, 1L, true));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Capacity must be an even number so teams can be split evenly", ex.getReason());
    }

    @Test
    void testUpdatePitch_Valid() {
        when(pitchRepository.findById(1L)).thenReturn(Optional.of(pitch));
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(pitchMapper.toResponseDTO(pitch)).thenReturn(updatePitchResponse);
        when(pitchRepository.save(pitch)).thenReturn(pitch);

        PitchResponseDTO result = pitchService.updatePitch(updatePitchRequest, 1L, true);


        assertEquals(1L, result.getId());
        assertEquals("Pitch 1 Updated", result.getName());
        assertEquals(20, result.getCapacity());
        assertEquals(1L, result.getLocationId());
        assertEquals("London", result.getLocationCity());
        assertEquals("Finsbury Park", result.getLocationArea());
        verify(pitchRepository).findById(1L);
        verify(locationRepository).findById(1L);
        verify(pitchMapper).toResponseDTO(pitch);
        verify(pitchRepository).save(pitch);
    }

    @Test
    void testSetActiveStatus_NotFound() {
        when(pitchRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> pitchService.setActiveStatus(1L, false));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Pitch not found", ex.getReason());
        verify(pitchRepository).findById(1L);
    }

    @Test
    void testSetActiveStatus_Valid() {
        pitchResponse.setActive(false);
        when(pitchRepository.findById(1L)).thenReturn(Optional.of(pitch));
        when(pitchRepository.save(pitch)).thenReturn(pitch);
        when(pitchMapper.toResponseDTO(pitch)).thenReturn(pitchResponse);

        PitchResponseDTO result = pitchService.setActiveStatus(1L, false);

        assertEquals(1L, result.getId());
        assertEquals(false, result.getActive());
        verify(pitchRepository).findById(1L);
        verify(pitchRepository).save(pitch);
        verify(pitchMapper).toResponseDTO(pitch);
    }

}