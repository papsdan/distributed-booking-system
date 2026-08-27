package com.dpapie01.distributed_booking_system.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enums represents refund policy set for a Game.
 * If a player withdraws after booking, the policy indicates the notice
 * period required before kickoff for a refund to be given.
 */
@Getter
@RequiredArgsConstructor
public enum RefundPolicy {
    NO_REFUND("No Refund"),
    HOURS_24("24 Hours"),
    HOURS_48("48 Hours");

    /** User friendly label shown in the UI.*/
    private final String label;
}