package com.dpapie01.distributed_booking_system.controller;

import com.dpapie01.distributed_booking_system.dto.BookingResponeDTO;
import com.dpapie01.distributed_booking_system.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/my-bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public String listBookings(@RequestParam(name = "withdrawSuccess", defaultValue = "false") boolean withdrawSuccess,
                                Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<BookingResponeDTO> bookings = bookingService.getMyBookings(userDetails.getUsername());
        model.addAttribute("bookings", bookings);
        if (withdrawSuccess) {
            model.addAttribute("successMessage", "You've withdrawn from this game.");
        }
        return "my-bookings";
    }

    @PostMapping("/{gameId}/withdraw")
    public String withdraw(@PathVariable Long gameId, RedirectAttributes redirectAttributes,
                            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            bookingService.withdrawSlot(gameId, userDetails.getUsername());
            return "redirect:/my-bookings?withdrawSuccess=true";
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getReason());
            return "redirect:/my-bookings";
        }
    }

}
