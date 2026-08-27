package com.dpapie01.distributed_booking_system.dto;

import com.dpapie01.distributed_booking_system.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * This class is the response DTO for a user account. Does not include the password hash intentionally for security purposes.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Role role;
    private Boolean active;
    private LocalDateTime createdAt;
}
