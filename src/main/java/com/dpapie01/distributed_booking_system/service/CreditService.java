package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.dto.CreditRequestDTO;
import com.dpapie01.distributed_booking_system.entity.Credit;
import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.repository.CreditRepository;
import com.dpapie01.distributed_booking_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
/**
 * This is a service class for managing user credit balances and top-ups.
 */
@RequiredArgsConstructor
@Service
public class CreditService {

    private final UserRepository userRepository;
    private final CreditRepository creditRepository;
    /**
     * Adds credits to the account balance of the specified user.
     * Creates and saves a new Credit record with the requested creditAmount and reason.
     * @param dto the credit request DTO containing the top-up amount
     * @param email the email of the user topping up credits
     */
    public void topUpCredits(CreditRequestDTO dto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Credit credit = new Credit();
        credit.setUser(user);
        credit.setAmount(dto.getCreditAmount());
        credit.setReason("Credit top-up");
        creditRepository.save(credit);
    }
}