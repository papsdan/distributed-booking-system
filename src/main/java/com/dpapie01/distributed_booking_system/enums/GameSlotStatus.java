package com.dpapie01.distributed_booking_system.enums;

/**
 * Enums represents status of a Game Slots.
 */
public enum GameSlotStatus {
    /** Free for a player to claim.*/
    AVAILABLE,
    /** Claimed by a player while their checkout is in progress.*/
    HELD,
    /** Claimed by a player whose booking has been confirmed.*/
    BOOKED
}