package com.dpapie01.distributed_booking_system.dto;

import com.dpapie01.distributed_booking_system.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponeDTO {
    private Long id;
    private Long gameId;
    private String gameTitle;
    private String pitchName;
    private String locationCity;
    private String locationArea;
    private LocalDate gameDate;
    private LocalTime gameTime;
    private Integer durationMinutes;
    private BookingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime withdrawnAt;
    private LocalDateTime expiresAt;
    private Boolean pastGame;
}
