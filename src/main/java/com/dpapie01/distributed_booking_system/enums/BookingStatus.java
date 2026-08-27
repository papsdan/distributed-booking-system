package com.dpapie01.distributed_booking_system.enums;

/**
 * Enums represents status of a Booking.
 */
public enum BookingStatus {
    /** When user at checkout.*/
    HELD,
    /** When user has successfully booked.*/
    CONFIRMED,
    /** When the checkout timer runs out.*/
    EXPIRED,
    /** When user withdraws from game.*/
    WITHDRAWN,
    /** When organiser cancels game.*/
    CANCELLED,
    /** When user abandons checkout.*/
    ABANDONED
}