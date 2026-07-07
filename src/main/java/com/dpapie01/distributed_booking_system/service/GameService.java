package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.dto.GameFilterDTO;
import com.dpapie01.distributed_booking_system.dto.GameRequestDTO;
import com.dpapie01.distributed_booking_system.dto.GameResponseDTO;
import com.dpapie01.distributed_booking_system.entity.Game;
import com.dpapie01.distributed_booking_system.entity.GameSlot;
import com.dpapie01.distributed_booking_system.entity.Pitch;
import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.enums.GameSlotStatus;
import com.dpapie01.distributed_booking_system.enums.GameStatus;
import com.dpapie01.distributed_booking_system.enums.PaymentType;
import com.dpapie01.distributed_booking_system.enums.RefundPolicy;
import com.dpapie01.distributed_booking_system.mapper.GameMapper;
import com.dpapie01.distributed_booking_system.repository.GameRepository;
import com.dpapie01.distributed_booking_system.repository.GameSlotRepository;
import com.dpapie01.distributed_booking_system.repository.PitchRepository;
import com.dpapie01.distributed_booking_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final GameSlotRepository gameSlotRepository;
    private final PitchRepository pitchRepository;
    private final UserRepository userRepository;
    private final GameMapper gameMapper;

    public List<GameResponseDTO> filterGames(GameFilterDTO filter) {
        return gameRepository.filterGames(
                        filter.getCity() == null || filter.getCity().isBlank() ? null : filter.getCity(),
                        filter.getArea() == null || filter.getArea().isBlank() ? null : filter.getArea(),
                        filter.getGameType(),
                        filter.getGenderOption(),
                        filter.getGameDate(),
                        filter.getMaxPrice()).stream()
                .map(game -> gameMapper.toResponseDTO(game,
                        countSlots(game, GameSlotStatus.BOOKED),
                        countSlots(game, GameSlotStatus.AVAILABLE)))
                .toList();
    }

    public GameResponseDTO getGameDetails(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

        return gameMapper.toResponseDTO(game,
                countSlots(game, GameSlotStatus.BOOKED),
                countSlots(game, GameSlotStatus.AVAILABLE));
    }

    public GameResponseDTO getGameForEdit(Long gameId, String organiserEmail) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

        assertOrganiser(game, organiserEmail);

        return gameMapper.toResponseDTO(game);
    }

    private int countSlots(Game game, GameSlotStatus status) {
        return (int) gameSlotRepository.countByGameAndStatus(game, status);
    }

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
        game.setRefundPolicy(RefundPolicy.NO_REFUND);

        Game savedGame = gameRepository.save(game);
        generateSlots(savedGame);

        return gameMapper.toResponseDTO(savedGame);
    }

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

    public GameResponseDTO updateGame(GameRequestDTO dto, Long gameId, String organiserEmail) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

        assertOrganiser(game, organiserEmail);

        Pitch pitch = validatePitchAndCapacity(dto);

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

        return gameMapper.toResponseDTO(gameRepository.save(game));
    }

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

    private void assertOrganiser(Game game, String organiserEmail) {
        if (!game.getOrganiser().getEmail().equals(organiserEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the organiser can edit this game");
        }
    }

    public void cancelGame(Long gameId, String organiserEmail) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

        assertOrganiser(game, organiserEmail);

        if (game.getStatus() == GameStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game is already cancelled");
        }
        if (LocalDateTime.of(game.getGameDate(), game.getGameTime()).isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game has already taken place");
        }

        game.setStatus(GameStatus.CANCELLED);
        gameRepository.save(game);
    }

}
