package com.dpapie01.distributed_booking_system.controller;

import com.dpapie01.distributed_booking_system.dto.ProfileResponseDTO;
import com.dpapie01.distributed_booking_system.dto.UpdateProfileRequestDTO;
import com.dpapie01.distributed_booking_system.entity.Location;
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

import java.util.List;

/**
 * This class is the controller for a user viewing and updating their own profile.
 */
@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final LocationRepository locationRepository;

    /**
     * Shows the current user's profile page, pre-filled with their current details.
     * @param updateSuccess if true, shows a profile updated success message
     */
    @GetMapping
    public String showProfile(@RequestParam(name = "updateSuccess", defaultValue = "false") boolean updateSuccess,
                               Model model, @AuthenticationPrincipal UserDetails userDetails) {
        ProfileResponseDTO profile = profileService.getProfile(userDetails.getUsername());

        UpdateProfileRequestDTO dto = new UpdateProfileRequestDTO();
        dto.setGender(profile.getGender());
        dto.setLocationId(profile.getPreferredLocationId());

        model.addAttribute("updateProfileRequestDto", dto);
        addLocationAttributes(model);
        model.addAttribute("genders", Gender.values());
        if (updateSuccess) {
            model.addAttribute("successMessage", "Profile updated successfully.");
        }
        return "profile";
    }

    /**
     * Submits the profile edit form. If validation fails, it re-shows the form with errors.
     * @param dto the submitted profile details
     */
    @PostMapping
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails, @Valid @ModelAttribute("updateProfileRequestDto") UpdateProfileRequestDTO dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            addLocationAttributes(model);
            model.addAttribute("genders", Gender.values());
            return "profile";
        }
        try {
            profileService.updateProfile(dto, userDetails.getUsername());
            return "redirect:/profile?updateSuccess=true";
        } catch (ResponseStatusException e) {
            model.addAttribute("errorMessage", e.getReason());
            addLocationAttributes(model);
            model.addAttribute("genders", Gender.values());
            return "profile";
        }
    }

    /** Private helper that populates the model with location dropdown options for the profile form.*/
    private void addLocationAttributes(Model model) {
        List<Location> locations = locationRepository.findAll();
        model.addAttribute("locations", locations);
        model.addAttribute("cities", locations.stream().map(Location::getCity).distinct().sorted().toList());
    }
}

