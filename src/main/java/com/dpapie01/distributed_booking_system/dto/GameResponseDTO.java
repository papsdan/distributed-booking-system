package com.dpapie01.distributed_booking_system.dto;

import com.dpapie01.distributed_booking_system.enums.GameGenderOption;
import com.dpapie01.distributed_booking_system.enums.GameStatus;
import com.dpapie01.distributed_booking_system.enums.GameType;
import com.dpapie01.distributed_booking_system.enums.PaymentType;
import com.dpapie01.distributed_booking_system.enums.RefundPolicy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Long pitchId;
    private String pitchName;
    private String locationCity;
    private String locationArea;
    private Long organiserId;
    private String organiserName;
    private LocalDate gameDate;
    private LocalTime gameTime;
    private Integer durationMinutes;
    private GameType gameType;
    private GameGenderOption genderOption;
    private Integer maxPlayers;
    private BigDecimal price;
    private PaymentType paymentType;
    private RefundPolicy refundPolicy;
    private GameStatus status;
}
