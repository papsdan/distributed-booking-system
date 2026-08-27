package com.dpapie01.distributed_booking_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a single credit ledger entry for a User.
 * Each row is an immutable transaction (e.g. a top-up or a refund) rather than a
 * running balance.
 * A user's total balance comes from summing their Credit amount entries.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credits")
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The user this credit transaction belongs to. */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Transaction amount (positive for a credit and refund, negative for paid online bookings). */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /** Reason for the transaction (e.g. "Refund for booking #123"). */
    @Column(nullable = false, length = 255)
    private String reason;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
