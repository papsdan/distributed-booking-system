package com.dpapie01.distributed_booking_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class is the response DTO for a pitch, including its location details.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PitchResponseDTO {
    private Long id;
    private String name;
    private Long locationId;
    private String locationCity;
    private String locationArea;
    private Integer capacity;
    private Boolean active;
}
