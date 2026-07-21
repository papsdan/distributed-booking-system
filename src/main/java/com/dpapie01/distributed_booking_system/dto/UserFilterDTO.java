package com.dpapie01.distributed_booking_system.dto;

import com.dpapie01.distributed_booking_system.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFilterDTO {

    private String searchQuery;
    private boolean activeOnly;
    private List<Role> roles;
}
