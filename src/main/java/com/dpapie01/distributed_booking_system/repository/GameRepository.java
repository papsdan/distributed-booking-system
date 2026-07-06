package com.dpapie01.distributed_booking_system.repository;

import com.dpapie01.distributed_booking_system.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
