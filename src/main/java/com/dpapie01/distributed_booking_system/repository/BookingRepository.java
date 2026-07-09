package com.dpapie01.distributed_booking_system.repository;

import com.dpapie01.distributed_booking_system.entity.Booking;
import com.dpapie01.distributed_booking_system.entity.Game;
import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBySlot_GameAndStatus(Game game, BookingStatus status);
    boolean existsBySlot_GameAndUserAndStatus(Game game, User user, BookingStatus status);
    List<Booking> findByUserAndStatus(User user, BookingStatus status);
    List<Booking> findByUser(User user);
}
