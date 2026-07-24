package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.dto.GameAttendeeDTO;
import com.dpapie01.distributed_booking_system.dto.GameFilterDTO;
import com.dpapie01.distributed_booking_system.dto.GameRequestDTO;
import com.dpapie01.distributed_booking_system.dto.GameResponseDTO;
import com.dpapie01.distributed_booking_system.entity.Booking;
import com.dpapie01.distributed_booking_system.entity.Game;
import com.dpapie01.distributed_booking_system.entity.GameSlot;
import com.dpapie01.distributed_booking_system.entity.Location;
import com.dpapie01.distributed_booking_system.entity.Pitch;
import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.enums.BookingStatus;
import com.dpapie01.distributed_booking_system.enums.GameGenderOption;
import com.dpapie01.distributed_booking_system.enums.GameSlotStatus;
import com.dpapie01.distributed_booking_system.enums.GameStatus;
import com.dpapie01.distributed_booking_system.enums.GameType;
import com.dpapie01.distributed_booking_system.enums.PaymentType;
import com.dpapie01.distributed_booking_system.enums.Role;
import com.dpapie01.distributed_booking_system.mapper.GameMapper;
import com.dpapie01.distributed_booking_system.repository.BookingRepository;
import com.dpapie01.distributed_booking_system.repository.CreditRepository;
import com.dpapie01.distributed_booking_system.repository.GameRepository;
import com.dpapie01.distributed_booking_system.repository.GameSlotRepository;
import com.dpapie01.distributed_booking_system.repository.PitchRepository;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private GameSlotRepository gameSlotRepository;
    @Mock
    private PitchRepository pitchRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GameMapper gameMapper;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CreditRepository creditRepository;

    @InjectMocks
    private GameService gameService;

    private Location location;
    private Pitch pitch;
    private User organiser;
    private User admin;
    private Game game;
    private GameResponseDTO gameResponse;
    private GameRequestDTO gameRequest;

    @BeforeEach
    void setUp() {
        location = new Location();
        location.setId(1L);
        location.setCity("London");
        location.setArea("Finsbury Park");

        pitch = new Pitch();
        pitch.setId(1L);
        pitch.setName("Pitch 1");
        pitch.setLocation(location);
        pitch.setCapacity(20);
        pitch.setActive(true);

        organiser = new User();
        organiser.setId(1L);
        organiser.setFirstName("Jon");
        organiser.setLastName("Smith");
        organiser.setEmail("jon@example.com");

        admin = new User();
        admin.setId(2L);
        admin.setFirstName("Ada");
        admin.setLastName("Admin");
        admin.setEmail("admin@example.com");
        admin.setRole(Role.ADMIN);

        game = new Game();
        game.setId(1L);
        game.setOrganiser(organiser);
        game.setPitch(pitch);
        game.setTitle("Sunday Game");
        game.setGameDate(LocalDate.now().plusDays(7));
        game.setGameTime(LocalTime.of(18, 0));
        game.setDurationMinutes(60);
        game.setGameType(GameType.TEN_A_SIDE);
        game.setGenderOption(GameGenderOption.MIXED);
        game.setMaxPlayers(20);
        game.setPrice(BigDecimal.ZERO);
        game.setPaymentType(PaymentType.FREE);

        gameResponse = new GameResponseDTO();
        gameResponse.setId(1L);
        gameResponse.setTitle("Sunday Game");
        gameResponse.setPitchId(1L);
        gameResponse.setMaxPlayers(20);

        gameRequest = new GameRequestDTO();
        gameRequest.setTitle("Sunday Game");
        gameRequest.setPitchId(1L);
        gameRequest.setGameDate(LocalDate.now().plusDays(7));
        gameRequest.setGameTime(LocalTime.of(18, 0));
        gameRequest.setDurationMinutes(60);
        gameRequest.setGameType(GameType.TEN_A_SIDE);
        gameRequest.setGenderOption(GameGenderOption.MIXED);
        gameRequest.setPrice(BigDecimal.ZERO);
        gameRequest.setPaymentType(PaymentType.FREE);
    }

    @Test
    void testCreateGame_UserNotFound() {
        when(userRepository.findByEmail("jon@example.com")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> gameService.createGame(gameRequest, "jon@example.com"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("User not found", ex.getReason());
    }

    @Test
    void testCreateGame_PitchNotFound() {
        when(userRepository.findByEmail("jon@example.com")).thenReturn(Optional.of(organiser));
        when(pitchRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> gameService.createGame(gameRequest, "jon@example.com"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Pitch not found", ex.getReason());
    }

    @Test
    void testCreateGame_PitchInactive() {
        pitch.setActive(false);
        when(userRepository.findByEmail("jon@example.com")).thenReturn(Optional.of(organiser));
        when(pitchRepository.findById(1L)).thenReturn(Optional.of(pitch));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> gameService.createGame(gameRequest, "jon@example.com"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Pitch is not active", ex.getReason());
    }

    @Test
    void testCreateGame_MaxPlayersExceedsPitchCapacity() {
        gameRequest.setGameType(GameType.ELEVEN_A_SIDE);
        when(userRepository.findByEmail("jon@example.com")).thenReturn(Optional.of(organiser));
        when(pitchRepository.findById(1L)).thenReturn(Optional.of(pitch));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> gameService.createGame(gameRequest, "jon@example.com"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Max players for this game type cannot exceed the pitch capacity", ex.getReason());
    }

    @Test
    void testCreateGame_FreeWithNonZeroPrice() {
        gameRequest.setPrice(BigDecimal.valueOf(5));
        when(userRepository.findByEmail("jon@example.com")).thenReturn(Optional.of(organiser));
        when(pitchRepository.findById(1L)).thenReturn(Optional.of(pitch));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> gameService.createGame(gameRequest, "jon@example.com"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Price must be 0 when payment type is Free", ex.getReason());
    }

    @Test
    void testCreateGame_PaidWithZeroPrice() {
        gameRequest.setPaymentType(PaymentType.CASH);
        when(userRepository.findByEmail("jon@example.com")).thenReturn(Optional.of(organiser));
        when(pitchRepository.findById(1L)).thenReturn(Optional.of(pitch));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> gameService.createGame(gameRequest, "jon@example.com"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Price must be greater than 0 for paid games", ex.getReason());
    }

    @Test
    void testCreateGame_Valid() {
        when(userRepository.findByEmail("jon@example.com")).thenReturn(Optional.of(organiser));
        when(pitchRepository.findById(1L)).thenReturn(Optional.of(pitch));
        when(gameRepository.save(any(Game.class))).thenReturn(game);
        when(gameMapper.toResponseDTO(game)).thenReturn(gameResponse);

        GameResponseDTO result = gameService.createGame(gameRequest, "jon@example.com");

        assertEquals(1L, result.getId());
        assertEquals("Sunday Game", result.getTitle());
        verify(gameMapper).toResponseDTO(game);
        verify(gameSlotRepository).saveAll(argThat((List<GameSlot> slots) ->
                slots.size() == game.getMaxPlayers()
                        && slots.stream().allMatch(slot -> slot.getGame() == game && slot.getStatus() == GameSlotStatus.AVAILABLE)));
    }

    @Test
    void testFilterGames() {
        GameFilterDTO filter = new GameFilterDTO();
        filter.setCity("London");
        filter.setArea("Finsbury Park");
        filter.setGameType(GameType.TEN_A_SIDE);
        filter.setGenderOption(GameGenderOption.MIXED);
        filter.setGameDate(LocalDate.of(2026, 7, 20));
        filter.setMaxPrice(BigDecimal.TEN);

        when(gameRepository.filterGames("London", "Finsbury Park", GameType.TEN_A_SIDE, GameGenderOption.MIXED,
                LocalDate.of(2026, 7, 20), BigDecimal.TEN, false))
                .thenReturn(List.of(game));
        when(gameSlotRepository.countByGameAndStatus(game, GameSlotStatus.BOOKED)).thenReturn(4L);
        when(gameSlotRepository.countByGameAndStatus(game, GameSlotStatus.AVAILABLE)).thenReturn(16L);
        when(gameSlotRepository.countByGameAndStatus(game, GameSlotStatus.HELD)).thenReturn(0L);
        when(gameMapper.toResponseDTO(game, 4, 16, 0)).thenReturn(gameResponse);

        List<GameResponseDTO> result = gameService.filterGames(filter);

        assertEquals(1, result.size());
        assertEquals("Sunday Game", result.getFirst().getTitle());
    }

    @Test
    void testFilterGames_OpenSlotsOnly() {
        GameFilterDTO filter = new GameFilterDTO();
        filter.setOpenSlotsOnly(true);

        when(gameRepository.filterGames(null, null, null, null, null, null, true)).thenReturn(List.of(game));
        when(gameSlotRepository.countByGameAndStatus(game, GameSlotStatus.BOOKED)).thenReturn(4L);
        when(gameSlotRepository.countByGameAndStatus(game, GameSlotStatus.AVAILABLE)).thenReturn(16L);
        when(gameSlotRepository.countByGameAndStatus(game, GameSlotStatus.HELD)).thenReturn(0L);
        when(gameMapper.toResponseDTO(game, 4, 16, 0)).thenReturn(gameResponse);

        List<GameResponseDTO> result = gameService.filterGames(filter);

        assertEquals(1, result.size());
        assertEquals("Sunday Game", result.getFirst().getTitle());
    }

    @Test
    void testFilterGames_NoFiltersSelected() {
        when(gameRepository.filterGames(null, null, null, null, null, null, false)).thenReturn(List.of(game));
        when(gameSlotRepository.countByGameAndStatus(game, GameSlotStatus.BOOKED)).thenReturn(0L);
        when(gameSlotRepository.countByGameAndStatus(game, GameSlotStatus.AVAILABLE)).thenReturn(20L);
        when(gameSlotRepository.countByGameAndStatus(game, GameSlotStatus.HELD)).thenReturn(0L);
        when(gameMapper.toResponseDTO(game, 0, 20, 0)).thenReturn(gameResponse);

        List<GameResponseDTO> result = gameService.filterGames(new GameFilterDTO());

        assertEquals(1, result.size());
        assertEquals("Sunday Game", result.getFirst().getTitle());
    }

    @Test
    void testGetGameDetails_Found() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(gameSlotRepository.countByGameAndStatus(game, GameSlotStatus.BOOKED)).thenReturn(4L);
        when(gameSlotRepository.countByGameAndStatus(game, GameSlotStatus.AVAILABLE)).thenReturn(16L);
        when(gameSlotRepository.countByGameAndStatus(game, GameSlotStatus.HELD)).thenReturn(0L);
        when(gameMapper.toResponseDTO(game, 4, 16, 0)).thenReturn(gameResponse);

        GameResponseDTO result = gameService.getGameDetails(1L);

        assertEquals(1L, result.getId());
        assertEquals("Sunday Game", result.getTitle());
    }

    @Test
    void testGetGameDetails_NotFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> gameService.getGameDetails(1L));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Game not found", ex.getReason());
    }

    @Test
    void testUpdateGame_NotFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> gameService.updateGame(gameRequest, 1L, "jon@example.com"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Game not found", ex.getReason());
    }

    @Test
    void testUpdateGame_NotOrganiser() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> gameService.updateGame(gameRequest, 1L, "notorganiser@example.com"));

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
        assertEquals("Only the organiser or an admin can edit this game", ex.getReason());
    }

    @Test
    void testUpdateGame_PitchNotFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(pitchRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> gameService.updateGame(gameRequest, 1L, "jon@example.com"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Pitch not found", ex.getReason());
    }

    @Test
    void testUpdateGame_Valid() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(pitchRepository.findById(1L)).thenReturn(Optional.of(pitch));
        when(gameRepository.save(game)).thenReturn(game);
        when(gameMapper.toResponseDTO(game)).thenReturn(gameResponse);

        GameResponseDTO result = gameService.updateGame(gameRequest, 1L, "jon@example.com");

        assertEquals(1L, result.getId());
        assertEquals("Sunday Game", result.getTitle());
        verify(gameRepository).save(game);
    }

    @Test
    void testUpdateGame_AdminNotOrganiser() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(admin));
        when(pitchRepository.findById(1L)).thenReturn(Optional.of(pitch));
        when(gameRepository.save(game)).thenReturn(game);
        when(gameMapper.toResponseDTO(game)).thenReturn(gameResponse);

        GameResponseDTO result = gameService.updateGame(gameRequest, 1L, "admin@example.com");

        assertEquals(1L, result.getId());
        verify(gameRepository).save(game);
    }

    @Test
    void testGetGameForEdit_NotFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> gameService.getGameForEdit(1L, "jon@example.com"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Game not found", ex.getReason());
    }

    @Test
    void testGetGameForEdit_NotOrganiser() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> gameService.getGameForEdit(1L, "someoneelse@example.com"));

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
        assertEquals("Only the organiser or an admin can edit this game", ex.getReason());
    }

    @Test
    void testGetGameForEdit_Valid() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(gameMapper.toResponseDTO(game)).thenReturn(gameResponse);

        GameResponseDTO result = gameService.getGameForEdit(1L, "jon@example.com");

        assertEquals(1L, result.getId());
        assertEquals("Sunday Game", result.getTitle());

    }

    @Test
    void testGetGameForEdit_AdminNotOrganiser() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(admin));
        when(gameMapper.toResponseDTO(game)).thenReturn(gameResponse);

        GameResponseDTO result = gameService.getGameForEdit(1L, "admin@example.com");

        assertEquals(1L, result.getId());
    }

    @Test
    void testCancelGame_NotFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> gameService.cancelGame(1L, "jon@example.com"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Game not found", ex.getReason());
    }

    @Test
    void testCancelGame_NotOrganiser() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> gameService.cancelGame(1L, "someoneelse@example.com"));

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
        assertEquals("Only the organiser or an admin can edit this game", ex.getReason());
    }

    @Test
    void testCancelGame_AlreadyCancelled() {
        game.setStatus(GameStatus.CANCELLED);
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> gameService.cancelGame(1L, "jon@example.com"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Game is already cancelled", ex.getReason());
    }

    @Test
    void testCancelGame_AlreadyPlayed() {
        game.setGameDate(LocalDate.of(2020, 1, 1));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> gameService.cancelGame(1L, "jon@example.com"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("Game has already taken place", ex.getReason());
    }

    @Test
    void testCancelGame_Valid() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        gameService.cancelGame(1L, "jon@example.com");

        assertEquals(GameStatus.CANCELLED, game.getStatus());
        verify(gameRepository).save(game);
    }

    @Test
    void testCancelGame_AdminNotOrganiser() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(admin));

        gameService.cancelGame(1L, "admin@example.com");

        assertEquals(GameStatus.CANCELLED, game.getStatus());
        verify(gameRepository).save(game);
    }

    @Test
    void testCancelGame_RefundsPaidBookings() {
        game.setPaymentType(PaymentType.PAID_ONLINE);
        game.setPrice(BigDecimal.valueOf(20));

        User player = new User();
        player.setId(2L);

        GameSlot slot = new GameSlot();
        slot.setGame(game);

        Booking booking = new Booking();
        booking.setSlot(slot);
        booking.setUser(player);
        booking.setAmountPaid(game.getPrice());
        booking.setStatus(BookingStatus.CONFIRMED);

        game.setPrice(BigDecimal.valueOf(30));

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(bookingRepository.findBySlot_GameAndStatus(game, BookingStatus.CONFIRMED)).thenReturn(List.of(booking));

        gameService.cancelGame(1L, "jon@example.com");

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        verify(bookingRepository).save(booking);
        verify(creditRepository).save(argThat(credit ->
                credit.getUser() == player && credit.getAmount().compareTo(BigDecimal.valueOf(20)) == 0));
    }

    @Test
    void testCancelGame_NoRefundForFreeGame() {
        User player = new User();
        player.setId(2L);

        GameSlot slot = new GameSlot();
        slot.setGame(game);

        Booking booking = new Booking();
        booking.setSlot(slot);
        booking.setUser(player);
        booking.setStatus(BookingStatus.CONFIRMED);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(bookingRepository.findBySlot_GameAndStatus(game, BookingStatus.CONFIRMED)).thenReturn(List.of(booking));

        gameService.cancelGame(1L, "jon@example.com");

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        verify(bookingRepository).save(booking);
        verify(creditRepository, never()).save(any());
    }

    @Test
    void testGetAttendees_NotFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> gameService.getAttendees(1L));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Game not found", ex.getReason());
    }

    @Test
    void testGetAttendees_NoBookings() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(bookingRepository.findBySlot_GameAndStatus(game, BookingStatus.CONFIRMED)).thenReturn(List.of());

        List<GameAttendeeDTO> result = gameService.getAttendees(1L);

        assertEquals(0, result.size());
    }

    @Test
    void testGetAttendees_ReturnsConfirmedBookings() {
        User attendee = new User();
        attendee.setId(2L);
        attendee.setFirstName("Jane");
        attendee.setLastName("Doe");
        attendee.setUsername("janedoe");
        attendee.setEmail("jane@example.com");

        GameSlot slot = new GameSlot();
        slot.setGame(game);
        slot.setStatus(GameSlotStatus.BOOKED);

        Booking booking = new Booking();
        booking.setSlot(slot);
        booking.setUser(attendee);
        booking.setStatus(BookingStatus.CONFIRMED);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(bookingRepository.findBySlot_GameAndStatus(game, BookingStatus.CONFIRMED)).thenReturn(List.of(booking));

        List<GameAttendeeDTO> result = gameService.getAttendees(1L);

        assertEquals(1, result.size());
        assertEquals(2L, result.getFirst().getId());
        assertEquals("Jane", result.getFirst().getFirstName());
        assertEquals("Doe", result.getFirst().getLastName());
        assertEquals("janedoe", result.getFirst().getUsername());
        assertEquals("jane@example.com", result.getFirst().getEmail());
    }
}
