package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.dto.BookingResponeDTO;
import com.dpapie01.distributed_booking_system.entity.Booking;
import com.dpapie01.distributed_booking_system.entity.Game;
import com.dpapie01.distributed_booking_system.entity.GameSlot;
import com.dpapie01.distributed_booking_system.entity.Profile;
import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.enums.BookingStatus;
import com.dpapie01.distributed_booking_system.enums.GameGenderOption;
import com.dpapie01.distributed_booking_system.enums.GameSlotStatus;
import com.dpapie01.distributed_booking_system.enums.GameStatus;
import com.dpapie01.distributed_booking_system.mapper.BookingMapper;
import com.dpapie01.distributed_booking_system.repository.BookingRepository;
import com.dpapie01.distributed_booking_system.repository.GameRepository;
import com.dpapie01.distributed_booking_system.repository.GameSlotRepository;
import com.dpapie01.distributed_booking_system.repository.ProfileRepository;
import com.dpapie01.distributed_booking_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookingService {

    private final GameRepository gameRepository;
    private final GameSlotRepository gameSlotRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final BookingMapper bookingMapper;

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

    public void withdrawSlot(Long gameId, String userEmail) {
        Game game = getGame(gameId);
        User user = getUser(userEmail);

        if (LocalDateTime.of(game.getGameDate(), game.getGameTime()).isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This game has already taken place");
        }

        Booking booking = bookingRepository.findBySlot_GameAndUserAndStatus(game, user, BookingStatus.CONFIRMED)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "You don't have a booking for this game"));

        GameSlot slot = booking.getSlot();
        slot.setStatus(GameSlotStatus.AVAILABLE);
        gameSlotRepository.save(slot);

        booking.setStatus(BookingStatus.WITHDRAWN);
        booking.setWithdrawnAt(LocalDateTime.now());
        bookingRepository.save(booking);
    }

    public String getJoinBlockReason(Long gameId, String userEmail) {
        Game game = getGame(gameId);
        User user = getUser(userEmail);
        return getJoinBlockReason(game, user);
    }

    public boolean hasConfirmedBooking(Long gameId, String userEmail) {
        Game game = getGame(gameId);
        User user = getUser(userEmail);
        return bookingRepository.existsBySlot_GameAndUserAndStatus(game, user, BookingStatus.CONFIRMED);
    }

    private String getJoinBlockReason(Game game, User user) {
        if (game.getStatus() == GameStatus.CANCELLED) {
            return "This game has been cancelled";
        }
        if (LocalDateTime.of(game.getGameDate(), game.getGameTime()).isBefore(LocalDateTime.now())) {
            return "This game has already taken place";
        }
        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));
        if (!profile.getGender().isEligibleFor(game.getGenderOption())) {
            String openTo = game.getGenderOption() == GameGenderOption.MENS ? "Men" : "Women";
            return "This game is open to " + openTo + " only";
        }
        if (bookingRepository.existsBySlot_GameAndUserAndStatus(game, user, BookingStatus.CONFIRMED)) {
            return "You have already booked into this game";
        }
        if (hasOverlappingBooking(game, user)) {
            return "You already have a booking that overlaps with this game's time";
        }
        if (gameSlotRepository.countByGameAndStatus(game, GameSlotStatus.AVAILABLE) == 0) {
            return "This game is full";
        }
        return null;
    }

    private boolean hasOverlappingBooking(Game game, User user) {
        LocalDateTime newBookingGameStart =  LocalDateTime.of(game.getGameDate(), game.getGameTime());
        LocalDateTime newBookingGameEnd = newBookingGameStart.plusMinutes(game.getDurationMinutes());

        return bookingRepository.findByUserAndStatus(user, BookingStatus.CONFIRMED).stream()
                .map(booking -> booking.getSlot().getGame())
                .filter(otherGame -> otherGame.getStatus() != GameStatus.CANCELLED)
                .anyMatch(otherGame -> {
                            LocalDateTime currentBookingGameStart = LocalDateTime.of(otherGame.getGameDate(), otherGame.getGameTime());
                            LocalDateTime currentBookingGameEnd = currentBookingGameStart.plusMinutes(otherGame.getDurationMinutes());
                            return newBookingGameEnd.isAfter(currentBookingGameStart) && newBookingGameStart.isBefore(currentBookingGameEnd);
                        }
                );
    }

    private Game getGame(Long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
    }
    private User getUser(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public List<BookingResponeDTO> getMyBookings(String userEmail) {
        User user = getUser(userEmail);
        List<Booking> bookings = bookingRepository.findByUser(user);
        return bookings.stream()
                .filter(booking -> !isSupersededWithdrawal(booking, bookings))
                .map(bookingMapper::toResponseDTO)
                .toList();
    }

    private boolean isSupersededWithdrawal(Booking booking, List<Booking> allBookings) {
        if (booking.getStatus() != BookingStatus.WITHDRAWN) {
            return false;
        }
        Long gameId = booking.getSlot().getGame().getId();
        return allBookings.stream()
                .anyMatch(other -> other != booking
                        && other.getSlot().getGame().getId().equals(gameId)
                        && other.getCreatedAt().isAfter(booking.getCreatedAt()));
    }
}
