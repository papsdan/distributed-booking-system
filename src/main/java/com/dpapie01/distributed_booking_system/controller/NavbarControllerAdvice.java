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

@ControllerAdvice
@RequiredArgsConstructor
public class NavbarControllerAdvice {

    private final UserRepository userRepository;
    private final CreditRepository creditRepository;

    @ModelAttribute("creditBalance")
    public BigDecimal creditBalance(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return null;
        }
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return creditRepository.sumAmountByUser(user);
    }
}