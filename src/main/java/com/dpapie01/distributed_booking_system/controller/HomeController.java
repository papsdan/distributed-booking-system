package com.dpapie01.distributed_booking_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This class is the controller for the site root, which redirects to the games listing automatically.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/games";
    }
}
