package com.dpapie01.distributed_booking_system.dto;

import com.dpapie01.distributed_booking_system.enums.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class is the request DTO for updating a user's profile.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequestDTO {

    @NotNull(message = "Please select your gender")
    private Gender gender;

    @NotNull(message = "Please select your preferred location")
    private Long locationId;
}
