package com.dpapie01.distributed_booking_system.controller;

import com.dpapie01.distributed_booking_system.dto.UserFilterDTO;
import com.dpapie01.distributed_booking_system.enums.Role;
import com.dpapie01.distributed_booking_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public String listUsers(@ModelAttribute("userFilterDto") UserFilterDTO filter, Model model) {
        model.addAttribute("users", userService.getAllUsers(filter));
        model.addAttribute("allRoles", Role.values());
        return "users";
    }
}
