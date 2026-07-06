package com.dpapie01.distributed_booking_system.mapper;

import com.dpapie01.distributed_booking_system.dto.GameResponseDTO;
import com.dpapie01.distributed_booking_system.entity.Game;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {

    public GameResponseDTO toResponseDTO(Game game) {
        GameResponseDTO dto = new GameResponseDTO();
        dto.setId(game.getId());
        dto.setTitle(game.getTitle());
        dto.setDescription(game.getDescription());
        dto.setPitchId(game.getPitch().getId());
        dto.setPitchName(game.getPitch().getName());
        dto.setLocationCity(game.getPitch().getLocation().getCity());
        dto.setLocationArea(game.getPitch().getLocation().getArea());
        dto.setOrganiserId(game.getOrganiser().getId());
        dto.setOrganiserName(game.getOrganiser().getFirstName() + " " + game.getOrganiser().getLastName());
        dto.setOrganiserEmail(game.getOrganiser().getEmail());
        dto.setGameDate(game.getGameDate());
        dto.setGameTime(game.getGameTime());
        dto.setDurationMinutes(game.getDurationMinutes());
        dto.setGameType(game.getGameType());
        dto.setGenderOption(game.getGenderOption());
        dto.setMaxPlayers(game.getMaxPlayers());
        dto.setPrice(game.getPrice());
        dto.setPaymentType(game.getPaymentType());
        dto.setRefundPolicy(game.getRefundPolicy());
        dto.setStatus(game.getStatus());
        return dto;
    }
}
