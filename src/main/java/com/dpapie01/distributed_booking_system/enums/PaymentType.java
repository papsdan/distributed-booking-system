package com.dpapie01.distributed_booking_system.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enums represents types of payment to join a Game.
 */
@Getter
@RequiredArgsConstructor
public enum PaymentType {
    FREE("Free"),
    CASH("Cash"),
    PAID_ONLINE("Paid Online");

    /** User friendly label shown in the UI.*/
    private final String label;
}