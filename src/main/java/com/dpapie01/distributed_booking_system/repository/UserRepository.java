package com.dpapie01.distributed_booking_system.repository;

import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', COALESCE(:searchQuery, u.firstName), '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', COALESCE(:searchQuery, u.lastName), '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', COALESCE(:searchQuery, u.username), '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', COALESCE(:searchQuery, u.email), '%'))) AND " +
            "(:activeOnly = false OR u.active = true) AND " +
            "(:roles IS NULL OR u.role IN :roles) " +
            "ORDER BY u.id")
    List<User> searchUsers(@Param("searchQuery") String searchQuery,
                            @Param("activeOnly") boolean activeOnly,
                            @Param("roles") List<Role> roles);
}
