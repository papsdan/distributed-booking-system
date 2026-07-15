package com.dpapie01.distributed_booking_system.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditRequestDTO {

    @NotNull
    @DecimalMin(value = "1", message = "Credit amount added must be at least 1.0")
    @DecimalMax(value = "100", message = "Credit amount added must not be greater than 100.0")
    private BigDecimal creditAmount;

}
