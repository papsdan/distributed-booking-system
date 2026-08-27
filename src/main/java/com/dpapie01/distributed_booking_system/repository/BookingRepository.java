package com.dpapie01.distributed_booking_system.repository;

import com.dpapie01.distributed_booking_system.entity.Booking;
import com.dpapie01.distributed_booking_system.entity.Game;
import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * This interface is the repository for Booking entities.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBySlot_GameAndStatus(Game game, BookingStatus status);
    boolean existsBySlot_GameAndUserAndStatus(Game game, User user, BookingStatus status);
    Optional<Booking> findBySlot_GameAndUserAndStatus(Game game, User user, BookingStatus status);
    List<Booking> findByUserAndStatus(User user, BookingStatus status);
    List<Booking> findByUser(User user);
    List<Booking> findByStatusAndExpiresAtBefore(BookingStatus status, LocalDateTime cutoff);

    /**
     * Attempts to acquire a Postgres transaction-scoped advisory lock, used by the
     * hold-expiry scheduler so that only one application instance runs the expiry sweep
     * at a time, avoiding redundant/duplicate updates across instances.
     * @param key the lock key
     * @return true if the lock was acquired, false if already held elsewhere
     */
    @Query(value = "SELECT pg_try_advisory_xact_lock(:key)", nativeQuery = true)
    boolean tryLock(@Param("key") long key);
}
