package com.dpapie01.distributed_booking_system.entity;

import com.dpapie01.distributed_booking_system.enums.GameSlotStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

/**
 * Represents a single slot in a Game.
 * The version field is a JPA optimistic-locking column used to prevent two
 * concurrent requests from claiming the same slot.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "game_slots")
public class GameSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Game this slot belongs to. */
    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    /** Current status of this slot (e.g. AVAILABLE, HELD, BOOKED). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GameSlotStatus status = GameSlotStatus.AVAILABLE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Optimistic-locking version, incremented on every update to guard against concurrent slot claims. */
    @Version
    @Column(nullable = false)
    private Long version;
}