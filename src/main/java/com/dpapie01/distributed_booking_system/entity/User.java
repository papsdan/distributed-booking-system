package com.dpapie01.distributed_booking_system.entity;

import com.dpapie01.distributed_booking_system.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a registered account in the system, used for authentication and
 * the owners of games, bookings and credits.
 * Additional player attributes (gender, preferred location) live in the
 * associated Profile.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    /** Hashed password used for authentication.*/
    @Column(nullable = false)
    private String password;

    /** Authorisation role determining what the user is permitted to do (e.g. PLAYER, ADMIN).*/
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.PLAYER;

    /** Active status showing if the account is currently active - deactivated users are prevented from logging in.*/
    @Column(nullable = false)
    private Boolean active = true;

    /** Time when the account was deactivated, if applicable. */
    @Column(name = "deactivated_at")
    private LocalDateTime deactivatedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}