package com.dpapie01.distributed_booking_system.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This class is the controller for the login page.
 */
@Controller
public class LoginController {

    /**
     * Shows the login page.
     * @param loginError if true, shows a login error message resolved from the session
     * @param logoutSuccess if true, shows a logout success message
     */
    @GetMapping("/login")
    public String loginPage(
            @RequestParam(name = "loginError", defaultValue = "false") final Boolean loginError,
            @RequestParam(name = "logoutSuccess", defaultValue = "false") final Boolean logoutSuccess,
            final HttpServletRequest request,
            final Model model) {

        if (loginError) {
            model.addAttribute("errorMessage", resolveErrorMessage(request));
        }
        if (logoutSuccess) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }

        return "login";
    }

    /** Provides a user-friendly error message to decipher between invalid credentials and deactivated account.*/
    private String resolveErrorMessage(final HttpServletRequest request) {
        final Object exception = request.getSession() != null
                ? request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION)
                : null;

        if (exception instanceof DisabledException) {
            return "Your account has been deactivated. Please contact support.";
        }

        return "Invalid email or password.";
    }
}
