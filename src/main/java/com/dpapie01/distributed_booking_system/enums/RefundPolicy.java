package com.dpapie01.distributed_booking_system.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RefundPolicy {
    NO_REFUND("No Refund"),
    HOURS_24("24 Hours"),
    HOURS_48("48 Hours");

    private final String label;
}