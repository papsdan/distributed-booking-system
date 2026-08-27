package com.dpapie01.distributed_booking_system.controller;

import com.dpapie01.distributed_booking_system.dto.CreditRequestDTO;
import com.dpapie01.distributed_booking_system.dto.GameFilterDTO;
import com.dpapie01.distributed_booking_system.dto.GameRequestDTO;
import com.dpapie01.distributed_booking_system.dto.GameResponseDTO;
import com.dpapie01.distributed_booking_system.dto.ProfileResponseDTO;
import com.dpapie01.distributed_booking_system.enums.GameGenderOption;
import com.dpapie01.distributed_booking_system.enums.GameType;
import com.dpapie01.distributed_booking_system.enums.PaymentType;
import com.dpapie01.distributed_booking_system.enums.RefundPolicy;
import com.dpapie01.distributed_booking_system.entity.Location;
import com.dpapie01.distributed_booking_system.repository.LocationRepository;
import com.dpapie01.distributed_booking_system.repository.PitchRepository;
import com.dpapie01.distributed_booking_system.service.BookingService;
import com.dpapie01.distributed_booking_system.service.CreditService;
import com.dpapie01.distributed_booking_system.service.GameService;
import com.dpapie01.distributed_booking_system.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This class is the controller for browsing, creating, editing, cancelling and booking games.
 */
@Controller
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final BookingService bookingService;
    private final CreditService creditService;
    private final PitchRepository pitchRepository;
    private final LocationRepository locationRepository;
    private final ProfileService profileService;

    /**
     * Shows the game listing page, filtered by the given criteria and populated with the
     * filter dropdown options (cities, areas, game types, gender options).
     * @param filter the filters currently applied to the listing
     * @param createSuccess if true, shows a game created success message
     * @param updateSuccess if true, shows a game updated success message
     * @param cancelSuccess if true, shows a game cancelled success message
     */
    @GetMapping
    public String listGames(@ModelAttribute("gameFilterDto") GameFilterDTO filter,
                             @RequestParam(name = "createSuccess", defaultValue = "false") boolean createSuccess,
                             @RequestParam(name = "updateSuccess", defaultValue = "false") boolean updateSuccess,
                             @RequestParam(name = "cancelSuccess", defaultValue = "false") boolean cancelSuccess,
                             Model model,
                             @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("games", gameService.filterGames(filter));
        model.addAttribute("currentUserEmail", userDetails.getUsername());
        ProfileResponseDTO profile = profileService.getProfile(userDetails.getUsername());
        model.addAttribute("preferredLocationCity", profile.getPreferredLocationCity());
        model.addAttribute("preferredLocationArea", profile.getPreferredLocationArea());
        List<Location> locations = locationRepository.findAll();
        model.addAttribute("cities", locations.stream()
                .map(Location::getCity).distinct().sorted().toList());
        model.addAttribute("areas", locations.stream()
                .filter(loc -> filter.getCity() == null || filter.getCity().isBlank() || loc.getCity().equals(filter.getCity()))
                .map(Location::getArea).distinct().sorted().toList());
        model.addAttribute("gameTypes", GameType.values());
        model.addAttribute("genderOptions", GameGenderOption.values());
        if (createSuccess) {
            model.addAttribute("successMessage", "Game created successfully.");
        }
        if (updateSuccess) {
            model.addAttribute("successMessage", "Game updated successfully.");
        }
        if (cancelSuccess) {
            model.addAttribute("successMessage", "Game cancelled successfully.");
        }
        return "games";
    }

    /** Shows the blank form for creating a new game.*/
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("gameRequestDto", new GameRequestDTO());
        addFormAttributes(model);
        return "game-form";
    }

    /**
     * Submits the create game form. If validation fails then it resows the form with errors
     * @param dto the submitted game details
     */
    @PostMapping
    public String createGame(@Valid @ModelAttribute("gameRequestDto") GameRequestDTO dto,
                              BindingResult result, Model model,
                              @AuthenticationPrincipal UserDetails userDetails) {
        if (result.hasErrors()) {
            addFormAttributes(model);
            return "game-form";
        }
        try {
            gameService.createGame(dto, userDetails.getUsername());
            return "redirect:/games?createSuccess=true";
        } catch (ResponseStatusException e) {
            model.addAttribute("errorMessage", e.getReason());
            addFormAttributes(model);
            return "game-form";
        }
    }

    /**
     * Shows the game details page, including attendees and the current user's
     * booking/join eligibility state.
     * @param id the game to show
     * @param bookSuccess if true, shows a booking success message
     * @param withdrawSuccess if true, shows a withdrawal success message
     */
    @GetMapping("/{id}")
    public String showGameDetails(@PathVariable Long id,
                                   @RequestParam(name = "bookSuccess", defaultValue = "false") boolean bookSuccess,
                                   @RequestParam(name = "withdrawSuccess", defaultValue = "false") boolean withdrawSuccess,
                                   Model model,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        GameResponseDTO game = gameService.getGameDetails(id);
        model.addAttribute("game", game);
        model.addAttribute("attendees", gameService.getAttendees(id));
        model.addAttribute("joinBlockReason", bookingService.getJoinBlockReason(id, userDetails.getUsername()));
        model.addAttribute("alreadyBooked", bookingService.hasConfirmedBooking(id, userDetails.getUsername()));
        model.addAttribute("withdrawConfirmMessage", bookingService.getWithdrawalOutcomeMessage(id));
        if (bookSuccess) {
            model.addAttribute("successMessage", "You're booked in!");
        }
        if (withdrawSuccess) {
            model.addAttribute("successMessage", "You've withdrawn from this game.");
        }
        return "game-details";
    }

    /**
     * Posts a HELD reservation on a game slot for the current user and sends them to checkout page.
     * @param id the game to book into
     */
    @PostMapping("/{id}/book")
    public String bookGame(@PathVariable Long id, RedirectAttributes redirectAttributes,
                            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            bookingService.holdSlot(id, userDetails.getUsername());
            return "redirect:/games/" + id + "/checkout";
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getReason());
            return "redirect:/games/" + id;
        }
    }

    /**
     * Shows the checkout page for the user's held slot. This includes the countdown
     * until the hold expires.
     * @param id the game the slot is being checked out for
     */
    @GetMapping("/{id}/checkout")
    public String showCheckout(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes,
                                @AuthenticationPrincipal UserDetails userDetails) {
        try {
            model.addAttribute("game", gameService.getGameDetails(id));
            LocalDateTime holdExpiresAt = bookingService.getHoldExpiresAt(id, userDetails.getUsername());
            long holdSecondsRemaining = Math.max(0, Duration.between(LocalDateTime.now(), holdExpiresAt).getSeconds());
            model.addAttribute("holdSecondsRemaining", holdSecondsRemaining);
            model.addAttribute("creditRequestDto", new CreditRequestDTO());
            return "checkout";
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getReason());
            return "redirect:/games/" + id;
        }
    }

    /**
     * Cancels the user's held slot and releases it back to the pool.
     * @param id the game the slot is being checked out for
     */
    @PostMapping("/{id}/checkout/cancel")
    public String cancelCheckout(@PathVariable Long id, RedirectAttributes redirectAttributes,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        try {
            bookingService.cancelSlot(id, userDetails.getUsername());
            return "redirect:/games/" + id;
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getReason());
            return "redirect:/games/" + id;
        }
    }

    /**
     * Confirms the current user's held slot, finalising the booking.
     * @param id the game the slot is being checked out for
     */
    @PostMapping("/{id}/checkout/confirm")
    public String confirmCheckout(@PathVariable Long id, RedirectAttributes redirectAttributes,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        try {
            bookingService.confirmSlot(id, userDetails.getUsername());
            return "redirect:/games/" + id + "?bookSuccess=true";
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getReason());
            return "redirect:/games/" + id + "/checkout";
        }
    }

    /**
     * When the checkout countdown reaches 0, expired
     * holds are released back to the pool immediately rather than waiting for the scheduled job.
     * @param id the game the slot is being checked out for
     */
    @PostMapping("/{id}/checkout/expire")
    public String expireCheckout(@PathVariable Long id) {
        bookingService.expireOverdueHeldBookings();
        return "redirect:/games/" + id;
    }

    /**
     * Withdraws the current user's confirmed booking from a game.
     * @param id the game to withdraw from
     */
    @PostMapping("/{id}/withdraw")
    public String withdrawGame(@PathVariable Long id, RedirectAttributes redirectAttributes,
                               @AuthenticationPrincipal UserDetails userDetails) {
        try {
            bookingService.withdrawSlot(id, userDetails.getUsername());
            return "redirect:/games/" + id + "?withdrawSuccess=true";
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getReason());
            return "redirect:/games/" + id;
        }
    }

    /**
     * Shows the edit form for a game, pre-filled with its current details.
     * @param id the game to edit
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        GameResponseDTO game = gameService.getGameForEdit(id, userDetails.getUsername());

        GameRequestDTO dto = new GameRequestDTO();
        dto.setTitle(game.getTitle());
        dto.setDescription(game.getDescription());
        dto.setPitchId(game.getPitchId());
        dto.setGameDate(game.getGameDate());
        dto.setGameTime(game.getGameTime());
        dto.setDurationMinutes(game.getDurationMinutes());
        dto.setGameType(game.getGameType());
        dto.setGenderOption(game.getGenderOption());
        dto.setPrice(game.getPrice());
        dto.setPaymentType(game.getPaymentType());
        dto.setRefundPolicy(game.getRefundPolicy());

        model.addAttribute("gameRequestDto", dto);
        model.addAttribute("gameId", id);
        model.addAttribute("hasActiveBookings", game.getHasActiveBookings());
        addFormAttributes(model);
        return "game-form";
    }

    /**
     * Submits the edit game form. If validation fails, it re-shows the form with errors.
     * @param id the game being updated
     * @param dto the submitted game details
     */
    @PostMapping("/{id}")
    public String updateGame(@PathVariable Long id,
                              @Valid @ModelAttribute("gameRequestDto") GameRequestDTO dto,
                              BindingResult result, Model model,
                              @AuthenticationPrincipal UserDetails userDetails) {
        if (result.hasErrors()) {
            model.addAttribute("gameId", id);
            addFormAttributes(model);
            return "game-form";
        }
        try {
            gameService.updateGame(dto, id, userDetails.getUsername());
            return "redirect:/games?updateSuccess=true";
        } catch (ResponseStatusException e) {
            model.addAttribute("errorMessage", e.getReason());
            model.addAttribute("gameId", id);
            addFormAttributes(model);
            return "game-form";
        }
    }

    /**
     * Cancels a game.
     * @param id the game to cancel
     */
    @PostMapping("/{id}/cancel")
    public String cancelGame(@PathVariable Long id, RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal UserDetails userDetails) {
        try {
            gameService.cancelGame(id, userDetails.getUsername());
            return "redirect:/games?cancelSuccess=true";
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getReason());
            return "redirect:/games";
        }
    }

    /**
     * Tops up the current user's credit balance from the checkout page.
     * @param id the game being checked out for
     * @param dto the amount to top up
     */
    @PostMapping("/{id}/checkout/topup")
    public String topUpCredits(@PathVariable Long id,
                               @Valid @ModelAttribute("creditRequestDto") CreditRequestDTO dto,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               @AuthenticationPrincipal UserDetails userDetails) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", result.getFieldError().getDefaultMessage());
            return "redirect:/games/" + id + "/checkout";
        }
        try {
            creditService.topUpCredits(dto, userDetails.getUsername());
            redirectAttributes.addFlashAttribute("successMessage", "You have successfully added " + dto.getCreditAmount() + " credits.");
            return "redirect:/games/" + id + "/checkout";
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getReason());
            return "redirect:/games/" + id + "/checkout";
        }
    }

    /** Private helper method which populates the model with dropdown options shared by the create and edit forms.*/
    private void addFormAttributes(Model model) {
        model.addAttribute("pitches", pitchRepository.findByActiveTrue());
        List<Location> locations = locationRepository.findAll();
        model.addAttribute("cities", locations.stream().map(Location::getCity).distinct().sorted().toList());
        model.addAttribute("locations", locations);
        model.addAttribute("gameTypes", GameType.values());
        model.addAttribute("genderOptions", GameGenderOption.values());
        model.addAttribute("paymentTypes", PaymentType.values());
        model.addAttribute("refundPolicies", RefundPolicy.values());
    }
}
