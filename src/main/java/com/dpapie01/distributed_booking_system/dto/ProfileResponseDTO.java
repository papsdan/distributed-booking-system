package com.dpapie01.distributed_booking_system.dto;

import com.dpapie01.distributed_booking_system.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class is the response DTO for a user's profile, including their preferred location details.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponseDTO {
    private Long id;
    private Gender gender;
    private Long preferredLocationId;
    private String preferredLocationCity;
    private String preferredLocationArea;
}
