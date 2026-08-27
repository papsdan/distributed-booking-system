package com.dpapie01.distributed_booking_system.controller;

import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.repository.CreditRepository;
import com.dpapie01.distributed_booking_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

/**
 * This class adds shared attributes (credit balance and logged-in username) to the model
 * of every request, so they're available to the navbar on any page.
 */
@ControllerAdvice
@RequiredArgsConstructor
public class NavbarControllerAdvice {

    private final UserRepository userRepository;
    private final CreditRepository creditRepository;

    /**
     * Resolves the current user's credit balance for display in the navbar.
     * @return the balance
     */
    @ModelAttribute("creditBalance")
    public BigDecimal creditBalance(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return null;
        }
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return creditRepository.sumAmountByUser(user);
    }

    /**
     * Resolves the current user's username for display in the navbar.
     * @return the username
     */
    @ModelAttribute("loggedInUsername")
    public String loggedInUsername(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return null;
        }
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"))
                .getUsername();
    }
}