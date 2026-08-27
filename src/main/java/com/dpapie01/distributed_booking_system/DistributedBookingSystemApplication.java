package com.dpapie01.distributed_booking_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
/**
 * This class is the entry point for the Distributed Booking System Spring Boot application.
 * Enables Spring Boot auto-configuration, component scanning and background task scheduling.
 */
@SpringBootApplication
@EnableScheduling
public class DistributedBookingSystemApplication {
    /**
     * Launches the Spring application context.
     */
	public static void main(String[] args) {
		SpringApplication.run(DistributedBookingSystemApplication.class, args);
	}

}
