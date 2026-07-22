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

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;
    private final LocationRepository locationRepository;

    @GetMapping
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequestDto", new RegisterRequestDTO());
        addLocationAttributes(model);
        model.addAttribute("genders", Gender.values());
        return "register";
    }

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

    private void addLocationAttributes(Model model) {
        List<Location> locations = locationRepository.findAll();
        model.addAttribute("locations", locations);
        model.addAttribute("cities", locations.stream().map(Location::getCity).distinct().sorted().toList());
    }
}
