package com.dpapie01.distributed_booking_system.mapper;

import com.dpapie01.distributed_booking_system.dto.BookingResponeDTO;
import com.dpapie01.distributed_booking_system.entity.Booking;
import com.dpapie01.distributed_booking_system.entity.Game;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BookingMapper {

    public BookingResponeDTO toResponseDTO(Booking booking) {
        Game game = booking.getSlot().getGame();
        BookingResponeDTO dto = new BookingResponeDTO();
        dto.setId(booking.getId());
        dto.setGameId(game.getId());
        dto.setGameTitle(game.getTitle());
        dto.setPitchName(game.getPitch().getName());
        dto.setLocationCity(game.getPitch().getLocation().getCity());
        dto.setLocationArea(game.getPitch().getLocation().getArea());
        dto.setGameDate(game.getGameDate());
        dto.setGameTime(game.getGameTime());
        dto.setDurationMinutes(game.getDurationMinutes());
        dto.setStatus(booking.getStatus());
        dto.setCreatedAt(booking.getCreatedAt());
        dto.setConfirmedAt(booking.getConfirmedAt());
        dto.setWithdrawnAt(booking.getWithdrawnAt());
        dto.setExpiresAt(booking.getExpiresAt());
        dto.setPastGame(LocalDateTime.of(game.getGameDate(), game.getGameTime()).isBefore(LocalDateTime.now()));
        return dto;
    }
}
