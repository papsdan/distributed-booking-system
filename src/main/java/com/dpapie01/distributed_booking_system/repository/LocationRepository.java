package com.dpapie01.distributed_booking_system.repository;

import com.dpapie01.distributed_booking_system.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This interface is the repository for Location entities.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
