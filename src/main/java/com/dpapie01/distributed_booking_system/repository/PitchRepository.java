package com.dpapie01.distributed_booking_system.repository;

import com.dpapie01.distributed_booking_system.entity.Location;
import com.dpapie01.distributed_booking_system.entity.Pitch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PitchRepository extends JpaRepository<Pitch, Long> {
    boolean existsByName(String name);
    boolean existsByLocation(Location location);
    boolean existsByNameAndLocation(String name, Location location);
    List<Pitch> findByActiveTrue();

}
