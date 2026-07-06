package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.dto.GameRequestDTO;
import com.dpapie01.distributed_booking_system.dto.GameResponseDTO;
import com.dpapie01.distributed_booking_system.entity.Game;
import com.dpapie01.distributed_booking_system.entity.Pitch;
import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.enums.PaymentType;
import com.dpapie01.distributed_booking_system.enums.RefundPolicy;
import com.dpapie01.distributed_booking_system.mapper.GameMapper;
import com.dpapie01.distributed_booking_system.repository.GameRepository;
import com.dpapie01.distributed_booking_system.repository.PitchRepository;
import com.dpapie01.distributed_booking_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final PitchRepository pitchRepository;
    private final UserRepository userRepository;
    private final GameMapper gameMapper;

    public List<GameResponseDTO> getAllGames() {
        return gameRepository.findAll(Sort.by("gameDate", "gameTime")).stream()
                .map(gameMapper::toResponseDTO)
                .toList();
    }

    public GameResponseDTO getGame(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

        return gameMapper.toResponseDTO(game);
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

        return gameMapper.toResponseDTO(gameRepository.save(game));
    }

    public GameResponseDTO updateGame(GameRequestDTO dto, Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

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

}
