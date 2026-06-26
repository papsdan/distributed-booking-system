package com.dpapie01.distributed_booking_system.dto;

import com.dpapie01.distributed_booking_system.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    @NotBlank
    @Size(max = 50, message ="First Name must not be more than 50 characters")
    private String firstName;

    @NotBlank
    @Size(max = 50, message ="Last Name must not be more than 50 characters")
    private String lastName;

    @NotBlank
    @Size(min = 3, max = 20, message ="Username must be between 3 and 20 characters")
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotNull
    private Gender gender;

    @NotNull
    private Long locationId;
}
