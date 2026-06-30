package com.dpapie01.distributed_booking_system.dto;

import com.dpapie01.distributed_booking_system.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponseDTO {
    private Long id;
    private Gender gender;
    private String preferredLocationCity;
    private String preferredLocationArea;
}
