package com.dpapie01.distributed_booking_system.entity;

import com.dpapie01.distributed_booking_system.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Holds Users profile information, including gender and preferred location (city/area).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The user this profile belongs to. */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /** Location the user prefers to see games flagged for. */
    @ManyToOne
    @JoinColumn(name = "preferred_location_id", nullable = false)
    private Location preferredLocation;

    /** User's gender, used when checking eligibility against a game's gender option. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Gender gender;
}