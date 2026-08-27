package com.dpapie01.distributed_booking_system.entity;

import com.dpapie01.distributed_booking_system.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a player's booking of a GameSlot within a Game.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The game slot this booking is for.*/
    @ManyToOne
    @JoinColumn(name = "slot_id", nullable = false)
    private GameSlot slot;

    /** The user who made the booking.*/
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Current status of the booking:
     * HELD when user at checkout
     * CONFIRMED when user has booked
     * EXPIRED when the checkout timer runs out
     * WITHDRAWN when user withdraws from game
     * CANCELLED when organiser cancels game
     * ABANDONED when user abandons checkout */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;

    /** Amount paid for the booking. */
    @Column(name = "amount_paid", precision = 10, scale = 2)
    private BigDecimal amountPaid;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Time when the HELD booking expires if not confirmed and becomes EXPIRED, if applicable. */
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    /** Time when the booking was CONFIRMED (checkout completed), if applicable */
    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    /** Time when the player WITHDRAW from the booking, if applicable. */
    @Column(name = "withdrawn_at")
    private LocalDateTime withdrawnAt;
}