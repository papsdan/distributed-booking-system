package com.dpapie01.distributed_booking_system.dto;

import com.dpapie01.distributed_booking_system.enums.GameGenderOption;
import com.dpapie01.distributed_booking_system.enums.GameType;
import com.dpapie01.distributed_booking_system.enums.PaymentType;
import com.dpapie01.distributed_booking_system.enums.RefundPolicy;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameRequestDTO {

    @NotBlank
    @Size(max = 150, message = "Title must not be more than 150 characters")
    private String title;

    private String description;

    @NotNull
    private Long pitchId;

    @NotNull
    @FutureOrPresent(message = "Game date must be today or in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate gameDate;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime gameTime;

    @NotNull
    @Min(value = 30, message = "Duration must be at least 30 minutes")
    @Max(value = 120, message = "Duration must not be over 120 minutes. If you require longer, you will need to create another game.")
    private Integer durationMinutes;

    @NotNull
    private GameType gameType;

    @NotNull
    private GameGenderOption genderOption;

    @NotNull
    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    private BigDecimal price;

    @NotNull
    private PaymentType paymentType;

    @NotNull
    private RefundPolicy refundPolicy;
}
