package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.entity.Booking;
import com.dpapie01.distributed_booking_system.entity.Game;
import com.dpapie01.distributed_booking_system.entity.GameSlot;
import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.enums.BookingStatus;
import com.dpapie01.distributed_booking_system.enums.GameSlotStatus;
import com.dpapie01.distributed_booking_system.enums.GameStatus;
import com.dpapie01.distributed_booking_system.repository.BookingRepository;
import com.dpapie01.distributed_booking_system.repository.GameRepository;
import com.dpapie01.distributed_booking_system.repository.GameSlotRepository;
import com.dpapie01.distributed_booking_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class BookingService {

    private final GameRepository gameRepository;
    private final GameSlotRepository gameSlotRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public void bookSlot(Long gameId, String userEmail) {
        Game game = getGame(gameId);
        User user = getUser(userEmail);

        String blockReason = getJoinBlockReason(game,user);
        if (blockReason != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, blockReason);
        }

        GameSlot slot = gameSlotRepository.findFirstByGameAndStatus(game, GameSlotStatus.AVAILABLE)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This game is full"));

        slot.setStatus(GameSlotStatus.BOOKED);
        gameSlotRepository.save(slot);

        Booking booking = new Booking();
        booking.setSlot(slot);
        booking.setUser(user);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setConfirmedAt(LocalDateTime.now());
        bookingRepository.save(booking);
    }

    public String getJoinBlockReason(Long gameId, String userEmail) {
        Game game = getGame(gameId);
        User user = getUser(userEmail);
        return getJoinBlockReason(game, user);
    }

    private String getJoinBlockReason(Game game, User user) {
        if (game.getStatus() == GameStatus.CANCELLED) {
            return "This game has been cancelled";
        }
        if (LocalDateTime.of(game.getGameDate(), game.getGameTime()).isBefore(LocalDateTime.now())) {
            return "This game has already taken place";
        }
        if (bookingRepository.existsBySlot_GameAndUserAndStatus(game, user, BookingStatus.CONFIRMED)) {
            return "You have already booked into this game";
        }
        if (gameSlotRepository.countByGameAndStatus(game, GameSlotStatus.AVAILABLE) == 0) {
            return "This game is full";
        }
        return null;
    }

    private Game getGame(Long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
    }
    private User getUser(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
