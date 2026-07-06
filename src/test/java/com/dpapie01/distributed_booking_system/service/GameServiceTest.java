package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.dto.GameRequestDTO;
import com.dpapie01.distributed_booking_system.dto.GameResponseDTO;
import com.dpapie01.distributed_booking_system.entity.Game;
import com.dpapie01.distributed_booking_system.entity.Location;
import com.dpapie01.distributed_booking_system.entity.Pitch;
import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.enums.GameGenderOption;
import com.dpapie01.distributed_booking_system.enums.GameType;
import com.dpapie01.distributed_booking_system.enums.PaymentType;
import com.dpapie01.distributed_booking_system.mapper.GameMapper;
import com.dpapie01.distributed_booking_system.repository.GameRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private PitchRepository pitchRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GameMapper gameMapper;

    @InjectMocks
    private GameService gameService;

    private Location location;
    private Pitch pitch;
    private User organiser;
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

        game = new Game();
        game.setId(1L);
        game.setOrganiser(organiser);
        game.setPitch(pitch);
        game.setTitle("Sunday Game");
        game.setGameDate(LocalDate.of(2026, 7, 20));
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
        gameRequest.setGameDate(LocalDate.of(2026, 7, 20));
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
        gameRequest.setPaymentType(PaymentType.PAID_ONLINE);
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
        assertEquals("Only the organiser can edit this game", ex.getReason());
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
        assertEquals("Only the organiser can edit this game", ex.getReason());
    }

    @Test
    void testGetGameForEdit_Valid() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(gameMapper.toResponseDTO(game)).thenReturn(gameResponse);

        GameResponseDTO result = gameService.getGameForEdit(1L, "jon@example.com");

        assertEquals(1L, result.getId());
        assertEquals("Sunday Game", result.getTitle());

    }
}
