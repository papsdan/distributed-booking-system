package com.dpapie01.distributed_booking_system.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PitchRequestDTO {

    @NotBlank
    @Size(max = 100, message = "Name must not be more than 100 characters")
    private String name;

    @NotNull
    private Long locationId;

    @NotNull
    @Min(value = 10, message = "Capacity must be at least 10 (5-aside)")
    @Max(value = 22, message = "Capacity must not be over 22 (11-aside)")
    private Integer capacity;
}
