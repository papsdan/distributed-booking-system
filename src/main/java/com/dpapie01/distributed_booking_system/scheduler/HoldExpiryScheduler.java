package com.dpapie01.distributed_booking_system.scheduler;

import com.dpapie01.distributed_booking_system.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HoldExpiryScheduler {

    private final BookingService bookingService;

    @Scheduled(fixedRate = 30000)
    public void expireOverdueHolds() {
        bookingService.expireOverdueHeldBookings();
    }
}
