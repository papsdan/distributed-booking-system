package com.dpapie01.distributed_booking_system.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameType {
    FIVE_A_SIDE(10, "5-a-side"),
    SIX_A_SIDE(12, "6-a-side"),
    SEVEN_A_SIDE(14, "7-a-side"),
    EIGHT_A_SIDE(16, "8-a-side"),
    NINE_A_SIDE(18, "9-a-side"),
    TEN_A_SIDE(20, "10-a-side"),
    ELEVEN_A_SIDE(22, "11-a-side");

    private final int maxPlayers;
    private final String label;

}