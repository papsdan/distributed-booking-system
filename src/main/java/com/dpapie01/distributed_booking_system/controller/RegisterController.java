package com.dpapie01.distributed_booking_system.controller;

import com.dpapie01.distributed_booking_system.dto.RegisterRequestDTO;
import com.dpapie01.distributed_booking_system.entity.Location;
import com.dpapie01.distributed_booking_system.enums.Gender;
import com.dpapie01.distributed_booking_system.repository.LocationRepository;
import com.dpapie01.distributed_booking_system.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * This class is the controller for new user registration.
 */
@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;
    private final LocationRepository locationRepository;

    /** Shows the blank registration form.*/
    @GetMapping
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequestDto", new RegisterRequestDTO());
        addLocationAttributes(model);
        model.addAttribute("genders", Gender.values());
        return "register";
    }

    /**
     * Submits the registration form. If validation fails, it re-shows the form with errors.
     * @param dto the submitted registration details
     */
    @PostMapping
    public String register(@Valid @ModelAttribute("registerRequestDto") RegisterRequestDTO dto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            addLocationAttributes(model);
            model.addAttribute("genders", Gender.values());
            return "register";
        }
        try {
            userService.register(dto);
            return "redirect:/login";
        } catch (ResponseStatusException e) {
            model.addAttribute("errorMessage", e.getReason());
            addLocationAttributes(model);
            model.addAttribute("genders", Gender.values());
            return "register";
        }
    }

    /** Private helper that populates the model with location dropdown options for the registration form.*/
    private void addLocationAttributes(Model model) {
        List<Location> locations = locationRepository.findAll();
        model.addAttribute("locations", locations);
        model.addAttribute("cities", locations.stream().map(Location::getCity).distinct().sorted().toList());
    }
}
