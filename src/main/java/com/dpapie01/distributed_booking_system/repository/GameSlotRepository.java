package com.dpapie01.distributed_booking_system.repository;

import com.dpapie01.distributed_booking_system.entity.Game;
import com.dpapie01.distributed_booking_system.entity.GameSlot;
import com.dpapie01.distributed_booking_system.enums.GameSlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameSlotRepository extends JpaRepository<GameSlot, Long> {
    long countByGameAndStatus(Game game, GameSlotStatus status);
    Optional<GameSlot> findFirstByGameAndStatus(Game game, GameSlotStatus status);
    List<GameSlot> findByGame(Game game);
}
