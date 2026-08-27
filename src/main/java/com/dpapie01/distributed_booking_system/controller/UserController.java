package com.dpapie01.distributed_booking_system.controller;

import com.dpapie01.distributed_booking_system.dto.UserFilterDTO;
import com.dpapie01.distributed_booking_system.dto.UserRequestDTO;
import com.dpapie01.distributed_booking_system.enums.Role;
import com.dpapie01.distributed_booking_system.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * This class is the controller for admins managing user accounts.
 */
@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Shows the user listing page, filtered by the given criteria.
     * @param filter the filters currently applied to the listing
     * @param updateSuccess if true, shows a user-updated success message
     */
    @GetMapping
    public String listUsers(@ModelAttribute("userFilterDto") UserFilterDTO filter,
                             @RequestParam(name = "updateSuccess", defaultValue = "false") boolean updateSuccess,
                             @AuthenticationPrincipal UserDetails userDetails,
                             Model model) {
        model.addAttribute("users", userService.getAllUsers(filter));
        model.addAttribute("allRoles", Role.values());
        model.addAttribute("currentUserEmail", userDetails.getUsername());
        if (updateSuccess) {
            model.addAttribute("successMessage", "User updated successfully.");
        }
        return "users";
    }

    /**
     * Submits an update to a user's role or active status. If validation fails, it re-shows the form with errors.
     * @param id the user being updated
     * @param dto the submitted role/active status
     * @param filter the filters currently applied to the listing, used to re-render it on error
     */
    @PostMapping("/{id}")
    public String updateUser(@PathVariable Long id,
                              @Valid @ModelAttribute("userRequestDto") UserRequestDTO dto,
                              BindingResult result,
                              @ModelAttribute("userFilterDto") UserFilterDTO filter,
                              @AuthenticationPrincipal UserDetails userDetails,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.getAllUsers(filter));
            model.addAttribute("allRoles", Role.values());
            model.addAttribute("currentUserEmail", userDetails.getUsername());
            return "users";
        }
        try {
            userService.updateUser(dto, id, userDetails.getUsername());
            return "redirect:/admin/users?updateSuccess=true";
        } catch (ResponseStatusException e) {
            model.addAttribute("errorMessage", e.getReason());
            model.addAttribute("users", userService.getAllUsers(filter));
            model.addAttribute("allRoles", Role.values());
            model.addAttribute("currentUserEmail", userDetails.getUsername());
            return "users";
        }
    }
}
