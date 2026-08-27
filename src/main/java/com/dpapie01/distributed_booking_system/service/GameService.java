package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.dto.GameAttendeeDTO;
import com.dpapie01.distributed_booking_system.dto.GameFilterDTO;
import com.dpapie01.distributed_booking_system.dto.GameRequestDTO;
import com.dpapie01.distributed_booking_system.dto.GameResponseDTO;
import com.dpapie01.distributed_booking_system.entity.Booking;
import com.dpapie01.distributed_booking_system.entity.Credit;
import com.dpapie01.distributed_booking_system.entity.Game;
import com.dpapie01.distributed_booking_system.entity.GameSlot;
import com.dpapie01.distributed_booking_system.entity.Pitch;
import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.enums.*;
import com.dpapie01.distributed_booking_system.mapper.GameMapper;
import com.dpapie01.distributed_booking_system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * This is a service class for searching, creating, updating, retrieving, cancelling games,
 * and fetching attendee lists.
 */
@RequiredArgsConstructor
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final GameSlotRepository gameSlotRepository;
    private final PitchRepository pitchRepository;
    private final UserRepository userRepository;
    private final GameMapper gameMapper;
    private final BookingRepository bookingRepository;
    private final CreditRepository creditRepository;
    /**
     * Filters games by location, game type, gender option, date, price and open slot availability.
     * Maps each game to a response DTO with calculated game slot counts for BOOKED, AVAILABLE and HELD statuses.
     * @param filter the game filter criteria
     * @return a list of filtered game response DTOs with slot breakdown
     */
    public List<GameResponseDTO> filterGames(GameFilterDTO filter) {
        return gameRepository.filterGames(
                        filter.getCity() == null || filter.getCity().isBlank() ? null : filter.getCity(),
                        filter.getArea() == null || filter.getArea().isBlank() ? null : filter.getArea(),
                        filter.getGameType(),
                        filter.getGenderOption(),
                        filter.getGameDate(),
                        filter.getMaxPrice(),
                        filter.isOpenSlotsOnly()).stream()
                .map(game -> gameMapper.toResponseDTO(game,
                        countSlots(game, GameSlotStatus.BOOKED),
                        countSlots(game, GameSlotStatus.AVAILABLE),
                        countSlots(game, GameSlotStatus.HELD)))
                .toList();
    }
    /**
     * Gets the details for a game including its current slot counts.
     * @param gameId the ID of the game
     * @return the game response DTO populated with slot counts
     */
    public GameResponseDTO getGameDetails(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

        return gameMapper.toResponseDTO(game,
                countSlots(game, GameSlotStatus.BOOKED),
                countSlots(game, GameSlotStatus.AVAILABLE),
                countSlots(game, GameSlotStatus.HELD));
    }
    /**
     * Gets game details for editing and validates that the user is the organiser or an admin.
     * Sets the hasActiveBookings flag on the response DTO to indicate whether restricted fields are editable.
     * @param gameId the ID of the game to edit
     * @param currentUserEmail the email of the user attempting to edit
     * @return the game response DTO with active booking state
     */
    public GameResponseDTO getGameForEdit(Long gameId, String currentUserEmail) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

        assertOrganiserOrAdmin(game, currentUserEmail);

        GameResponseDTO dto = gameMapper.toResponseDTO(game);
        dto.setHasActiveBookings(hasActiveBookings(game));
        return dto;
    }
    /**
     * Private helper which counts the number of slots for a game with a given GameSlotStatus.
     * @param game the game to count slots for
     * @param status the slot status to count
     * @return the count of matching slots
     */
    private int countSlots(Game game, GameSlotStatus status) {
        return (int) gameSlotRepository.countByGameAndStatus(game, status);
    }
    /**
     * Creates a new game and generates AVAILABLE game slots up to maxPlayers.
     * Validates if pitch exists, pitch capacity and price constraints before saving.
     * @param dto the game creation details
     * @param organiserEmail the email of the organising user
     * @return the created game response DTO
     */
    public GameResponseDTO createGame(GameRequestDTO dto, String organiserEmail) {
        User organiser = userRepository.findByEmail(organiserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Pitch pitch = validatePitchAndCapacity(dto);

        Game game = new Game();
        game.setOrganiser(organiser);
        game.setPitch(pitch);
        game.setTitle(dto.getTitle());
        game.setDescription(dto.getDescription());
        game.setGameDate(dto.getGameDate());
        game.setGameTime(dto.getGameTime());
        game.setDurationMinutes(dto.getDurationMinutes());
        game.setGameType(dto.getGameType());
        game.setGenderOption(dto.getGenderOption());
        game.setMaxPlayers(dto.getGameType().getMaxPlayers());
        game.setPrice(dto.getPrice());
        game.setPaymentType(dto.getPaymentType());
        game.setRefundPolicy(dto.getRefundPolicy());

        Game savedGame = gameRepository.save(game);
        generateSlots(savedGame);

        return gameMapper.toResponseDTO(savedGame);
    }
    /**
     * Private helper which generates and saves AVAILABLE GameSlot entities up to maxPlayers.
     * @param game the game to generate slots for
     */
    private void generateSlots(Game game) {
        List<GameSlot> slots = new ArrayList<>();
        for (int i = 0; i < game.getMaxPlayers(); i++) {
            GameSlot slot = new GameSlot();
            slot.setGame(game);
            slot.setStatus(GameSlotStatus.AVAILABLE);
            slots.add(slot);
        }
        gameSlotRepository.saveAll(slots);
    }
    /**
     * Updates an existing game after validating permissions (e.g. admin or organiser only) and pitch capacity constraints.
     * Prevents changes to pitch, gameType or genderOption if the game has active HELD or CONFIRMED bookings.
     * @param dto the updated game details
     * @param gameId the ID of the game being updated
     * @param currentUserEmail the email of the user updating the game
     * @return the updated game response DTO
     */
    public GameResponseDTO updateGame(GameRequestDTO dto, Long gameId, String currentUserEmail) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

        assertOrganiserOrAdmin(game, currentUserEmail);

        Pitch pitch = validatePitchAndCapacity(dto);

        boolean restrictedFieldChanged =
                !game.getPitch().getId().equals(dto.getPitchId()) ||
                game.getGameType() != dto.getGameType() ||
                game.getGenderOption() != dto.getGenderOption();

        if (restrictedFieldChanged && hasActiveBookings(game)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Pitch, game type and gender option cannot be changed once players have joined or are checking out.");
        }

        game.setPitch(pitch);
        game.setTitle(dto.getTitle());
        game.setDescription(dto.getDescription());
        game.setGameDate(dto.getGameDate());
        game.setGameTime(dto.getGameTime());
        game.setDurationMinutes(dto.getDurationMinutes());
        game.setGameType(dto.getGameType());
        game.setGenderOption(dto.getGenderOption());
        game.setMaxPlayers(dto.getGameType().getMaxPlayers());
        game.setPrice(dto.getPrice());
        game.setPaymentType(dto.getPaymentType());
        game.setRefundPolicy(dto.getRefundPolicy());

        return gameMapper.toResponseDTO(gameRepository.save(game));
    }
    /**
     * Private helper which checks if a game has any active HELD or CONFIRMED bookings.
     * @param game the game being checked
     * @return true if active bookings exist, false otherwise
     */
    private boolean hasActiveBookings(Game game) {
        return !bookingRepository.findBySlot_GameAndStatus(game, BookingStatus.HELD).isEmpty() ||
                !bookingRepository.findBySlot_GameAndStatus(game, BookingStatus.CONFIRMED).isEmpty();
    }
    /**
     * Private helper which validates pitch, capacity limits and pricing rules based on PaymentType.
     * @param dto the game request DTO to validate
     * @return the validated Pitch entity
     */
    private Pitch validatePitchAndCapacity(GameRequestDTO dto) {
        Pitch pitch = pitchRepository.findById(dto.getPitchId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pitch not found"));

        if (!pitch.getActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pitch is not active");
        }
        if (dto.getGameType().getMaxPlayers() > pitch.getCapacity()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Max players for this game type cannot exceed the pitch capacity");
        }
        if (dto.getPaymentType() == PaymentType.FREE && dto.getPrice().compareTo(BigDecimal.ZERO) != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price must be 0 when payment type is Free");
        }
        if (dto.getPaymentType() != PaymentType.FREE && dto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price must be greater than 0 for paid games");
        }

        return pitch;
    }
    /**
     * Private helper which checks if the user is the organiser of the game.
     * @param game the game to check
     * @param currentUserEmail the email of the user
     * @return true if the user is the organiser, false otherwise
     */
    private boolean isOrganiser(Game game, String currentUserEmail) {
        return game.getOrganiser().getEmail().equals(currentUserEmail);
    }
    /**
     * Private helper which checks if the user has the ADMIN role.
     * @param currentUserEmail the email of the user
     * @return true if the user is an admin, false otherwise
     */
    private boolean isAdmin(String currentUserEmail) {
        return userRepository.findByEmail(currentUserEmail)
                .map(user -> user.getRole() == Role.ADMIN)
                .orElse(false);
    }
    /**
     * Private helper which asserts that the user is either the organiser or an admin.
     * @param game the game being edited
     * @param currentUserEmail the email of the user
     */
    private void assertOrganiserOrAdmin(Game game, String currentUserEmail) {
        if (!isOrganiser(game, currentUserEmail) && !isAdmin(currentUserEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the organiser or an admin can edit this game");
        }
    }
    /**
     * Cancels a game, marks its status as CANCELLED and issues refunds to all confirmed attendees.
     * Validates that the user is an organiser/admin, the game is not already cancelled and it has not already taken place.
     * @param gameId the ID of the game to cancel
     * @param currentUserEmail the email of the user cancelling the game
     */
    public void cancelGame(Long gameId, String currentUserEmail) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

        assertOrganiserOrAdmin(game, currentUserEmail);

        if (game.getStatus() == GameStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game is already cancelled");
        }
        if (LocalDateTime.of(game.getGameDate(), game.getGameTime()).isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game has already taken place");
        }

        game.setStatus(GameStatus.CANCELLED);
        gameRepository.save(game);

        refundConfirmedBookings(game);
    }
    /**
     * Private helper which updates CONFIRMED bookings to CANCELLED and refunds credits if PAID_ONLINE.
     * @param game the cancelled game
     */
    private void refundConfirmedBookings(Game game) {
        List<Booking> confirmedBookings = bookingRepository.findBySlot_GameAndStatus(game, BookingStatus.CONFIRMED);
        for (Booking booking : confirmedBookings) {
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);

            if (game.getPaymentType() == PaymentType.PAID_ONLINE) {
                Credit refund = new Credit();
                refund.setUser(booking.getUser());
                refund.setAmount(booking.getAmountPaid());
                refund.setReason("Refund for cancelled game: " + game.getTitle());
                creditRepository.save(refund);
            }
        }
    }
    /**
     * Gets all attendees with CONFIRMED bookings for a game.
     * @param gameId the ID of the game
     * @return a list of attendee DTOs
     */
    public List<GameAttendeeDTO> getAttendees(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
        return bookingRepository.findBySlot_GameAndStatus(game, BookingStatus.CONFIRMED).stream()
                .map(booking -> new GameAttendeeDTO(
                        booking.getUser().getId(),
                        booking.getUser().getFirstName(),
                        booking.getUser().getLastName(),
                        booking.getUser().getUsername(),
                        booking.getUser().getEmail())
                )
                .toList();
    }

}
