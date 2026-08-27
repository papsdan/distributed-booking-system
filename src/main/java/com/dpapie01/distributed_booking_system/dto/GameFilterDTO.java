package com.dpapie01.distributed_booking_system.dto;

import com.dpapie01.distributed_booking_system.enums.GameGenderOption;
import com.dpapie01.distributed_booking_system.enums.GameType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * This class is the request DTO for filtering the game search/listing page. All fields
 * are optional.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameFilterDTO {

    private String city;
    private String area;
    private GameType gameType;
    private GameGenderOption genderOption;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate gameDate;

    private BigDecimal maxPrice;

    /** Boolean check whether the game has open slots. If true, only include games that still have an available slot.*/
    private boolean openSlotsOnly;
}
