package com.dpapie01.distributed_booking_system.entity;

import com.dpapie01.distributed_booking_system.enums.GameSlotStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GameSlotStatus status = GameSlotStatus.AVAILABLE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}