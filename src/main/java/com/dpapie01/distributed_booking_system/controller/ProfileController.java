package com.dpapie01.distributed_booking_system.controller;

import com.dpapie01.distributed_booking_system.dto.ProfileResponseDTO;
import com.dpapie01.distributed_booking_system.dto.UpdateProfileRequestDTO;
import com.dpapie01.distributed_booking_system.enums.Gender;
import com.dpapie01.distributed_booking_system.repository.LocationRepository;
import com.dpapie01.distributed_booking_system.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;


@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final LocationRepository locationRepository;

    @GetMapping
    public String showProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        ProfileResponseDTO profile = profileService.getProfile(userDetails.getUsername());

        UpdateProfileRequestDTO dto = new UpdateProfileRequestDTO();
        dto.setGender(profile.getGender());
        dto.setLocationId(profile.getPreferredLocationId());

        model.addAttribute("updateProfileRequestDto", dto);
        model.addAttribute("locations", locationRepository.findAll());
        model.addAttribute("genders", Gender.values());
        return "profile";
    }

    @PostMapping
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails, @Valid @ModelAttribute("updateProfileRequestDto") UpdateProfileRequestDTO dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("locations", locationRepository.findAll());
            model.addAttribute("genders", Gender.values());
            return "profile";
        }
        try {
            profileService.updateProfile(dto, userDetails.getUsername());
            return "redirect:/profile";
        } catch (ResponseStatusException e) {
            model.addAttribute("errorMessage", e.getReason());
            model.addAttribute("locations", locationRepository.findAll());
            model.addAttribute("genders", Gender.values());
            return "profile";
        }
    }
}

