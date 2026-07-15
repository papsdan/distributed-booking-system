package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.dto.CreditRequestDTO;
import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.repository.CreditRepository;
import com.dpapie01.distributed_booking_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CreditRepository creditRepository;

    @InjectMocks
    private CreditService creditService;

    private User user;
    private CreditRequestDTO dto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("jane@example.com");

        dto = new CreditRequestDTO();
        dto.setCreditAmount(BigDecimal.valueOf(25));
    }

    @Test
    void testTopUpCredits_UserNotFound() {
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> creditService.topUpCredits(dto, "jane@example.com"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("User not found", ex.getReason());
    }

    @Test
    void testTopUpCredits_Valid() {
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));

        creditService.topUpCredits(dto, "jane@example.com");

        verify(creditRepository).save(argThat(credit ->
                credit.getUser() == user
                        && credit.getAmount().compareTo(BigDecimal.valueOf(25)) == 0
                        && credit.getReason() != null && !credit.getReason().isBlank()));
    }
}
