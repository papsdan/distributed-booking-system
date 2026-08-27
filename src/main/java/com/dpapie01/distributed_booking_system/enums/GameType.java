package com.dpapie01.distributed_booking_system.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enums represents Game types (e.g. 5-a-side).
 * Each carries the max player count (e.g. 5-a-side = 10 players) and a display label for that format.
 */
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

    /** Maximum player count for both teams in this format (e.g. 5-a-side = 10 players)*/
    private final int maxPlayers;
    /** User friendly label shown in the UI.*/
    private final String label;

}