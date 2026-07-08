package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.entity.Game;
import com.dpapie01.distributed_booking_system.entity.GameSlot;
import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.enums.BookingStatus;
import com.dpapie01.distributed_booking_system.enums.GameGenderOption;
import com.dpapie01.distributed_booking_system.enums.GameSlotStatus;
import com.dpapie01.distributed_booking_system.enums.GameStatus;
import com.dpapie01.distributed_booking_system.enums.GameType;
import com.dpapie01.distributed_booking_system.enums.PaymentType;
import com.dpapie01.distributed_booking_system.repository.BookingRepository;
import com.dpapie01.distributed_booking_system.repository.GameRepository;
import com.dpapie01.distributed_booking_system.repository.GameSlotRepository;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private GameSlotRepository gameSlotRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingService bookingService;

    private Game game;
    private User user;
    private GameSlot slot;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.setId(1L);
        game.setGameDate(LocalDate.of(2026, 7, 20));
        game.setGameTime(LocalTime.of(18, 0));
        game.setGameType(GameType.TEN_A_SIDE);
        game.setGenderOption(GameGenderOption.MIXED);
        game.setMaxPlayers(10);
        game.setPrice(BigDecimal.ZERO);
        game.setPaymentType(PaymentType.FREE);
        game.setStatus(GameStatus.OPEN);

        user = new User();
        user.setId(2L);
        user.setEmail("jane@example.com");

        slot = new GameSlot();
        slot.setId(5L);
        slot.setGame(game);
        slot.setStatus(GameSlotStatus.AVAILABLE);
    }

    @Test
    void testBookSlot_GameNotFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookingService.bookSlot(1L, "jane@example.com"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Game not found", ex.getReason());
    }

    @Test
    void testBookSlot_UserNotFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookingService.bookSlot(1L, "jane@example.com"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("User not found", ex.getReason());
    }

    @Test
    void testBookSlot_GameCancelled() {
        game.setStatus(GameStatus.CANCELLED);
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookingService.bookSlot(1L, "jane@example.com"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("This game has been cancelled", ex.getReason());
    }

    @Test
    void testBookSlot_GameAlreadyPlayed() {
        game.setGameDate(LocalDate.of(2020, 1, 1));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookingService.bookSlot(1L, "jane@example.com"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("This game has already taken place", ex.getReason());
    }

    @Test
    void testBookSlot_GameFull() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(gameSlotRepository.findFirstByGameAndStatus(game, GameSlotStatus.AVAILABLE)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookingService.bookSlot(1L, "jane@example.com"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("This game is full", ex.getReason());
    }

    @Test
    void testBookSlot_Valid() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(gameSlotRepository.findFirstByGameAndStatus(game, GameSlotStatus.AVAILABLE)).thenReturn(Optional.of(slot));

        bookingService.bookSlot(1L, "jane@example.com");

        assertEquals(GameSlotStatus.BOOKED, slot.getStatus());
        verify(gameSlotRepository).save(slot);
        verify(bookingRepository).save(argThat(booking ->
                booking.getSlot() == slot
                        && booking.getUser() == user
                        && booking.getStatus() == BookingStatus.CONFIRMED
                        && booking.getConfirmedAt() != null));
    }

}
