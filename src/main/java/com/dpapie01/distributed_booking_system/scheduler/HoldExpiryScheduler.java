package com.dpapie01.distributed_booking_system.scheduler;

import com.dpapie01.distributed_booking_system.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
/**
 * This class is the scheduled task runner for automatically sweeping expired booking holds.
 */
@RequiredArgsConstructor
@Component
public class HoldExpiryScheduler {

    private final BookingService bookingService;
    /**
     * Scheduled method which calls BookingService.expireOverdueHeldBookings() every 30 seconds (fixedRate = 30000).
     * Releases any overdue HELD booking game slots back to AVAILABLE status across distributed instances.
     */
    @Scheduled(fixedRate = 30000)
    public void expireOverdueHolds() {
        bookingService.expireOverdueHeldBookings();
    }
}
