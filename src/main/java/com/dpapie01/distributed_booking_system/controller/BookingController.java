package com.dpapie01.distributed_booking_system.controller;

import com.dpapie01.distributed_booking_system.dto.BookingResponeDTO;
import com.dpapie01.distributed_booking_system.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/my-bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public String listBookings(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<BookingResponeDTO> bookings = bookingService.getMyBookings(userDetails.getUsername());
        model.addAttribute("bookings", bookings);
        return "my-bookings";
    }

}
