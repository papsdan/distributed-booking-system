package com.dpapie01.distributed_booking_system.controller;

import com.dpapie01.distributed_booking_system.dto.ProfileResponseDTO;
import com.dpapie01.distributed_booking_system.dto.UpdateProfileRequestDTO;
import com.dpapie01.distributed_booking_system.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileResponseDTO> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        ProfileResponseDTO response = profileService.getProfile(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<ProfileResponseDTO> updateProfile(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody UpdateProfileRequestDTO dto) {
        ProfileResponseDTO response = profileService.updateProfile(dto,userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

}


