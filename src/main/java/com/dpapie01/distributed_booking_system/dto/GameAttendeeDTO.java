package com.dpapie01.distributed_booking_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class is the response DTO for a player attending a game (i.e. with a confirmed booking).
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameAttendeeDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
}
