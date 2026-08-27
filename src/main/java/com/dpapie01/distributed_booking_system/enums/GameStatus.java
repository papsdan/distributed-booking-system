package com.dpapie01.distributed_booking_system.enums;

/**
 * Enums represents status of a Game
 **/
public enum GameStatus {
    /** Accepting new bookings.*/
    OPEN,
    /** All slots are currently HELD or BOOKED, so no longer accepting new bookings.*/
    FULL,
    /** The game was cancelled by the organiser.*/
    CANCELLED,
    /** The game has taken place.*/
    COMPLETED
}