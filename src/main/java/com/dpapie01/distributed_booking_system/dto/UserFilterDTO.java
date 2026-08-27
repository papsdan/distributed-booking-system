package com.dpapie01.distributed_booking_system.dto;

import com.dpapie01.distributed_booking_system.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * This class is the request DTO for filtering the user search/listing page. All fields
 * are optional.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFilterDTO {

    private String searchQuery;

    /** Boolean check whether user is active. If true, only include active users.*/
    private boolean activeOnly;
    private List<Role> roles;
}
