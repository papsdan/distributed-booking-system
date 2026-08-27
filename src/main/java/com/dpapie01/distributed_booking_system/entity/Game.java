package com.dpapie01.distributed_booking_system.entity;

import com.dpapie01.distributed_booking_system.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Represents a game organised on a Pitch by a User (the organiser).
 * A game defines the rules players must satisfy to join (gender option, capacity, price,
 * payment type) with its own GameStatus.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Pitch the game is played on. */
    @ManyToOne
    @JoinColumn(name = "pitch_id", nullable = false)
    private Pitch pitch;

    /** User who created and manages this game. */
    @ManyToOne
    @JoinColumn(name = "organiser_id", nullable = false)
    private User organiser;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "game_date", nullable = false)
    private LocalDate gameDate;

    @Column(name = "game_time", nullable = false)
    private LocalTime gameTime;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    /** Game type (e.g. 5-a-side, 11-a-side). */
    @Enumerated(EnumType.STRING)
    @Column(name = "game_type", nullable = false, length = 20)
    private GameType gameType;

    /** Gender option (e.g. MEN, WOMEN, MIXED) - eligibility rule applied when players attempt to join this game. */
    @Enumerated(EnumType.STRING)
    @Column(name = "gender_option", nullable = false, length = 10)
    private GameGenderOption genderOption;

    /** Maximum number of players who may hold/confirm a booking for this game. */
    @Column(name = "max_players", nullable = false)
    private Integer maxPlayers;

    /** Price per player. */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;

    /** How players are expected to pay to join (e.g. FREE, CASH, PAID_ONLINE). */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false, length = 20)
    private PaymentType paymentType;

    /** Refund Policy applied if a player withdraws after booking. */
    @Enumerated(EnumType.STRING)
    @Column(name = "refund_policy", nullable = false, length = 20)
    private RefundPolicy refundPolicy;

    /** Current game state of the game (e.g. OPEN, FULL, CANCELLED, COMPLETED). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GameStatus status = GameStatus.OPEN;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}