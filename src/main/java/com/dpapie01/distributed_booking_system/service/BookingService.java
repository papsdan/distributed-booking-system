package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.dto.BookingResponeDTO;
import com.dpapie01.distributed_booking_system.entity.Booking;
import com.dpapie01.distributed_booking_system.entity.Credit;
import com.dpapie01.distributed_booking_system.entity.Game;
import com.dpapie01.distributed_booking_system.entity.GameSlot;
import com.dpapie01.distributed_booking_system.entity.Profile;
import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.enums.BookingStatus;
import com.dpapie01.distributed_booking_system.enums.GameGenderOption;
import com.dpapie01.distributed_booking_system.enums.GameSlotStatus;
import com.dpapie01.distributed_booking_system.enums.GameStatus;
import com.dpapie01.distributed_booking_system.enums.PaymentType;
import com.dpapie01.distributed_booking_system.enums.RefundPolicy;
import com.dpapie01.distributed_booking_system.mapper.BookingMapper;
import com.dpapie01.distributed_booking_system.repository.BookingRepository;
import com.dpapie01.distributed_booking_system.repository.CreditRepository;
import com.dpapie01.distributed_booking_system.repository.GameRepository;
import com.dpapie01.distributed_booking_system.repository.GameSlotRepository;
import com.dpapie01.distributed_booking_system.repository.ProfileRepository;
import com.dpapie01.distributed_booking_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
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
    private final CreditRepository creditRepository;
    private final BookingMapper bookingMapper;

    private static final int HOLD_MINUTES = 3;

    @Transactional
    public void holdSlot(Long gameId, String userEmail){
        Game game = getGame(gameId);
        User user = getUser(userEmail);

        String blockReason = getJoinBlockReason(game,user);
        if (blockReason != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, blockReason);
        }

        GameSlot slot = gameSlotRepository.findFirstByGameAndStatus(game, GameSlotStatus.AVAILABLE)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "This game is full"));

        slot.setStatus(GameSlotStatus.HELD);
        try {
            gameSlotRepository.saveAndFlush(slot);
        } catch (OptimisticLockingFailureException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This slot was just taken by another player, please try again");
        }

        Booking booking = new Booking();
        booking.setSlot(slot);
        booking.setUser(user);
        booking.setStatus(BookingStatus.HELD);
        booking.setExpiresAt(LocalDateTime.now().plusMinutes(HOLD_MINUTES));
        bookingRepository.save(booking);
    }

    @Transactional
    public void confirmSlot(Long gameId, String userEmail){
        Game game = getGame(gameId);
        User user = getUser(userEmail);

        Booking booking = bookingRepository.findBySlot_GameAndUserAndStatus(game, user, BookingStatus.HELD)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "You don't have a held booking for this game"));

        if(booking.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your booking slot hold has expired. Please try again joining again");
        }

        if (game.getPaymentType() == PaymentType.PAID_ONLINE) {
            BigDecimal userBalance = creditRepository.sumAmountByUser(user);
            if (userBalance.compareTo(game.getPrice()) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You don't have enough credits for this game");
            }
        }

        GameSlot slot = booking.getSlot();
        slot.setStatus(GameSlotStatus.BOOKED);
        gameSlotRepository.save(slot);

        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setConfirmedAt(LocalDateTime.now());
        booking.setAmountPaid(game.getPrice());
        bookingRepository.save(booking);

        if (game.getPaymentType() == PaymentType.PAID_ONLINE) {
            Credit payment = new Credit();
            payment.setUser(user);
            payment.setAmount(game.getPrice().negate());
            payment.setReason("Booking payment for " + game.getTitle());
            creditRepository.save(payment);
        }
    }

    @Transactional
    public void cancelSlot(Long gameId, String userEmail) {
        Game game = getGame(gameId);
        User user = getUser(userEmail);

        Booking booking = bookingRepository.findBySlot_GameAndUserAndStatus(game, user, BookingStatus.HELD)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "You don't have a held booking for this game"));

        GameSlot slot = booking.getSlot();
        slot.setStatus(GameSlotStatus.AVAILABLE);
        gameSlotRepository.save(slot);

        booking.setStatus(BookingStatus.ABANDONED);
        bookingRepository.save(booking);
    }

    public LocalDateTime getHoldExpiresAt(Long gameId, String userEmail) {
        Game game = getGame(gameId);
        User user = getUser(userEmail);
        return bookingRepository.findBySlot_GameAndUserAndStatus(game, user, BookingStatus.HELD)
                .map(Booking::getExpiresAt)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "You don't have a held booking for this game"));
    }

    @Transactional
    public void expireOverdueHeldBookings() {
        List<Booking> overdueHeldBookings = bookingRepository.findByStatusAndExpiresAtBefore(BookingStatus.HELD, LocalDateTime.now());
        for (Booking booking : overdueHeldBookings) {
            GameSlot slot = booking.getSlot();
            slot.setStatus(GameSlotStatus.AVAILABLE);
            gameSlotRepository.save(slot);

            booking.setStatus(BookingStatus.EXPIRED);
            bookingRepository.save(booking);
        }
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

        if (game.getPaymentType() == PaymentType.PAID_ONLINE && isWithinRefundWindow(game)) {
            Credit refund = new Credit();
            refund.setUser(user);
            refund.setAmount(booking.getAmountPaid());
            refund.setReason("Refund for withdrawing from " + game.getTitle());
            creditRepository.save(refund);
        }
    }

    private boolean isWithinRefundWindow(Game game) {
        if (game.getRefundPolicy() == RefundPolicy.NO_REFUND) {
            return false;
        }
        int hours = game.getRefundPolicy() == RefundPolicy.HOURS_24 ? 24 : 48;
        LocalDateTime cutoff = LocalDateTime.of(game.getGameDate(), game.getGameTime()).minusHours(hours);
        return LocalDateTime.now().isBefore(cutoff);
    }

    public String getWithdrawalOutcomeMessage(Long gameId) {
        return getWithdrawalOutcomeMessage(getGame(gameId));
    }

    private String getWithdrawalOutcomeMessage(Game game) {
        String withdrawQuestion = "Are you sure you want to withdraw?";

        if (game.getPaymentType() != PaymentType.PAID_ONLINE) {
            return withdrawQuestion;
        }
        if (game.getRefundPolicy() == RefundPolicy.NO_REFUND) {
            return withdrawQuestion + " There are no refunds for this game, so you will not be refunded.";
        }
        if (isWithinRefundWindow(game)) {
            return withdrawQuestion + " You will be refunded " + game.getPrice() + " credits.";
        }
        return withdrawQuestion + " This game's refund window has passed, so you will not be refunded.";
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
        if (bookingRepository.existsBySlot_GameAndUserAndStatus(game, user, BookingStatus.HELD)) {
            return "You already have an active checkout in progress for this game";
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
                .map(booking -> {
                    BookingResponeDTO dto = bookingMapper.toResponseDTO(booking);
                    if (booking.getStatus() == BookingStatus.CONFIRMED) {
                        dto.setWithdrawConfirmMessage(getWithdrawalOutcomeMessage(booking.getSlot().getGame()));
                    }
                    return dto;
                })
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
