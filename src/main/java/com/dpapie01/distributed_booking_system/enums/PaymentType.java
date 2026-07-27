package com.dpapie01.distributed_booking_system.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentType {
    FREE("Free"),
    CASH("Cash"),
    PAID_ONLINE("Paid Online");

    private final String label;
}