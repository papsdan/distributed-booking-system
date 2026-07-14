package com.dpapie01.distributed_booking_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DistributedBookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistributedBookingSystemApplication.class, args);
	}

}
