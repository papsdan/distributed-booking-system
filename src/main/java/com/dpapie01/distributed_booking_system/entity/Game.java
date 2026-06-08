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

    @ManyToOne
    @JoinColumn(name = "pitch_id", nullable = false)
    private Pitch pitch;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "game_type", nullable = false, length = 20)
    private GameType gameType;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_option", nullable = false, length = 10)
    private GameGenderOption genderOption;

    @Column(name = "max_players", nullable = false)
    private Integer maxPlayers;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false, length = 20)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "refund_policy", nullable = false, length = 20)
    private RefundPolicy refundPolicy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GameStatus status = GameStatus.OPEN;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}