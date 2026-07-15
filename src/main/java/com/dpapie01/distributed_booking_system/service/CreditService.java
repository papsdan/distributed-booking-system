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

@RequiredArgsConstructor
@Service
public class CreditService {

    private final UserRepository userRepository;
    private final CreditRepository creditRepository;

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