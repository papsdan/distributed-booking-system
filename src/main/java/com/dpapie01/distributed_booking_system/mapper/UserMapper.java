package com.dpapie01.distributed_booking_system.mapper;

import com.dpapie01.distributed_booking_system.dto.UserResponseDTO;
import com.dpapie01.distributed_booking_system.entity.User;
import org.springframework.stereotype.Component;

/**
 * This class maps entities to UserResponseDTO for HTTP request responses.
 */
@Component
public class UserMapper {

    /**
     * Converts a user into a response DTO. It intentionally does not include the password hash for security purposes.
     * @param user the user
     * @return the mapped response DTO
     */
    public UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setActive(user.getActive());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
