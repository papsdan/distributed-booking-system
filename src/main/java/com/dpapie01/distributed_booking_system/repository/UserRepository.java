package com.dpapie01.distributed_booking_system.repository;

import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This interface is the repository for User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    /**
     * Searches for users matching a free-text query against name/username/email, optionally
     * filtered to active users only and/or restricted to a set of roles.
     * @param searchQuery text to match against first name, last name, username or email
     *                     (case-insensitive substring match), or null to match any
     * @param activeOnly if true, only include active users
     * @param roles roles to restrict the search to, or null for any role
     * @return matching users ordered by id
     */
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
