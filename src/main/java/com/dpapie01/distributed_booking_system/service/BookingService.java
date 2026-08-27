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
/**
 * This is service class for holding slots, confirming bookings, handling cancellations,
 * processing withdrawals and managing expired booking holds.
 */
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
    private static final long HOLD_EXPIRY_JOB_LOCK = 12345L;

    /**
     * Places a temporary hold for HOLD_MINUTES on the first available slot in a game.
     * Validates the user can join based on getJoinBlockReason eligibility before reserving the slot.
     * It uses JPA optimistic locking to prevent double-holding under concurrent requests.
     * @param gameId the ID of the game
     * @param userEmail the email of the user holding the slot
     */
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

    /**
     * Confirms an active HELD slot.
     * Validates that the hold has not expired, profile gender is eligible for genderOption and checks user has sufficient balance if PAID_ONLINE payment type.
     * It marks the slot as BOOKED, updates booking status to CONFIRMED and deducts credits if required.
     * @param gameId the ID of the game
     * @param userEmail the email of the user confirming the slot
     */
    @Transactional
    public void confirmSlot(Long gameId, String userEmail){
        Game game = getGame(gameId);
        User user = getUser(userEmail);

        Booking booking = bookingRepository.findBySlot_GameAndUserAndStatus(game, user, BookingStatus.HELD)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "You don't have a held booking for this game"));

        if(booking.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your booking slot hold has expired. Please try again joining again");
        }
        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));
        if (!profile.getGender().isEligibleFor(game.getGenderOption())) {
            String openTo = game.getGenderOption() == GameGenderOption.MEN ? "Men" : "Women";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This game is open to " + openTo + " only");
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
    /**
     * Cancels an active HELD slot and resets the status to AVAILABLE.
     * Updates the booking status to ABANDONED so other players can take the slot.
     * @param gameId the ID of the game
     * @param userEmail the email of the user cancelling the slot hold
     */
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
    /**
     * Gets the expiration date and time for a user's active HELD booking.
     * @param gameId the ID of the game
     * @param userEmail the email of the user
     * @return the expiration timestamp of the hold
     */
    public LocalDateTime getHoldExpiresAt(Long gameId, String userEmail) {
        Game game = getGame(gameId);
        User user = getUser(userEmail);
        return bookingRepository.findBySlot_GameAndUserAndStatus(game, user, BookingStatus.HELD)
                .map(Booking::getExpiresAt)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "You don't have a held booking for this game"));
    }
    /**
     * Gets all overdue HELD bookings and releases their game slots back to AVAILABLE with the booking status EXPIRED.
     * It uses PostgreSQL advisory lock with HOLD_EXPIRY_JOB_LOCK to ensure single execution across multiple instances.
     */
    @Transactional
    public void expireOverdueHeldBookings() {
        if(!bookingRepository.tryLock(HOLD_EXPIRY_JOB_LOCK)){
            return;
        }
        List<Booking> overdueHeldBookings = bookingRepository.findByStatusAndExpiresAtBefore(BookingStatus.HELD, LocalDateTime.now());
        for (Booking booking : overdueHeldBookings) {
            GameSlot slot = booking.getSlot();
            slot.setStatus(GameSlotStatus.AVAILABLE);
            gameSlotRepository.save(slot);

            booking.setStatus(BookingStatus.EXPIRED);
            bookingRepository.save(booking);
        }
    }
    /**
     * Withdraws a user from a CONFIRMED booking and resets the game slot back to AVAILABLE.
     * Updates booking status to WITHDRAWN and issues a credit refund if PAID_ONLINE and within the refund window.
     * @param gameId the ID of the game
     * @param userEmail the email of the user withdrawing
     */
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
    /**
     * Private helper which checks if the current time is before the refund cutoff based on RefundPolicy.
     * @param game the game to evaluate
     * @return true if eligible for a refund, false otherwise
     */
    private boolean isWithinRefundWindow(Game game) {
        if (game.getRefundPolicy() == RefundPolicy.NO_REFUND) {
            return false;
        }
        int hours = game.getRefundPolicy() == RefundPolicy.HOURS_24 ? 24 : 48;
        LocalDateTime cutoff = LocalDateTime.of(game.getGameDate(), game.getGameTime()).minusHours(hours);
        return LocalDateTime.now().isBefore(cutoff);
    }
    /**
     * Gets the withdrawal confirmation message explaining refund outcome for a game.
     * @param gameId the ID of the game
     * @return the withdrawal outcome prompt message
     */
    public String getWithdrawalOutcomeMessage(Long gameId) {
        return getWithdrawalOutcomeMessage(getGame(gameId));
    }
    /**
     * Private helper which constructs the withdrawal confirmation message based on PaymentType and refund window.
     * @param game the game to evaluate
     * @return the confirmation prompt text
     */
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
    /**
     * Checks if a user is blocked from joining a game.
     * @param gameId the ID of the game
     * @param userEmail the email of the user
     * @return the block reason message or null if eligible to join
     */
    public String getJoinBlockReason(Long gameId, String userEmail) {
        Game game = getGame(gameId);
        User user = getUser(userEmail);
        return getJoinBlockReason(game, user);
    }
    /**
     * Private helper which checks game cancellation, past dates, gender eligibility, duplicate bookings, active holds, time overlaps and slot capacity.
     * @param game the game being checked
     * @param user the user attempting to join
     * @return the block reason message or null if eligible
     */
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
            String openTo = game.getGenderOption() == GameGenderOption.MEN ? "Men" : "Women";
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
    /**
     * Checks whether a user already has a CONFIRMED booking for a game.
     * @param gameId the ID of the game
     * @param userEmail the email of the user
     * @return true if a confirmed booking exists, false otherwise
     */
    public boolean hasConfirmedBooking(Long gameId, String userEmail) {
        Game game = getGame(gameId);
        User user = getUser(userEmail);
        return bookingRepository.existsBySlot_GameAndUserAndStatus(game, user, BookingStatus.CONFIRMED);
    }
    /**
     * Private helper which checks if the user has another confirmed booking that overlaps with the game's start and end time.
     * @param game the target game
     * @param user the user being checked
     * @return true if there is an overlap, false otherwise
     */
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
    /** Private helper which gets a Game by ID or throws NOT_FOUND.*/
    private Game getGame(Long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
    }
    /** Private helper which gets a User by email or throws NOT_FOUND.*/
    private User getUser(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
    /**
     * Gets all bookings for a user. It filters out superseded withdrawals and adding withdrawal messages for confirmed bookings.
     * @param userEmail the email of the user
     * @return the list of booking response DTOs
     */
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
    /**
     * Private helper which checks if a WITHDRAWN booking was superseded by a newer booking for the same game so it is not duplicated.
     * @param booking the booking to check
     * @param allBookings the list of all user bookings
     * @return true if superseded by a newer booking, false otherwise
     */
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
