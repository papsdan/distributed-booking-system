package com.dpapie01.distributed_booking_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(
            @RequestParam(name = "loginError", defaultValue = "false") final Boolean loginError,
            @RequestParam(name = "logoutSuccess", defaultValue = "false") final Boolean logoutSuccess,
            final Model model) {

        if (loginError) {
            model.addAttribute("errorMessage", "Invalid email or password.");
        }
        if (logoutSuccess) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }

        return "login";
    }
}
