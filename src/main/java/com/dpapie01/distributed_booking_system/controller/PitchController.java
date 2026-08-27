package com.dpapie01.distributed_booking_system.controller;

import com.dpapie01.distributed_booking_system.dto.PitchRequestDTO;
import com.dpapie01.distributed_booking_system.dto.PitchResponseDTO;
import com.dpapie01.distributed_booking_system.entity.Location;
import com.dpapie01.distributed_booking_system.repository.LocationRepository;
import com.dpapie01.distributed_booking_system.service.PitchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * This class is the controller for admins managing pitches.
 */
@Controller
@RequestMapping("/admin/pitches")
@RequiredArgsConstructor
public class PitchController {

    private final PitchService pitchService;
    private final LocationRepository locationRepository;

    /**
     * Shows the pitch listing page.
     * @param createSuccess if true, shows a pitch created success message
     * @param updateSuccess if true, shows a pitch updated success message
     */
    @GetMapping
    public String listPitches(@RequestParam(name = "createSuccess", defaultValue = "false") boolean createSuccess,
                               @RequestParam(name = "updateSuccess", defaultValue = "false") boolean updateSuccess,
                               Model model) {
        model.addAttribute("pitches", pitchService.getAllPitches());
        if (createSuccess) {
            model.addAttribute("successMessage", "Pitch created successfully.");
        }
        if (updateSuccess) {
            model.addAttribute("successMessage", "Pitch updated successfully.");
        }
        return "pitches";
    }

    /** Shows the blank form for creating a new pitch. */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("pitchRequestDto", new PitchRequestDTO());
        addLocationAttributes(model);
        return "pitch-form";
    }

    /**
     * Submits the create-pitch form. If validation fails, it re-shows the form with errors
     * @param dto the submitted pitch details
     */
    @PostMapping
    public String createPitch(@Valid @ModelAttribute("pitchRequestDto") PitchRequestDTO dto,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            addLocationAttributes(model);
            return "pitch-form";
        }
        try {
            pitchService.createPitch(dto);
            return "redirect:/admin/pitches?createSuccess=true";
        } catch (ResponseStatusException e) {
            model.addAttribute("errorMessage", e.getReason());
            addLocationAttributes(model);
            return "pitch-form";
        }
    }

    /**
     * Shows the edit form for a pitch, pre-filled with its current details.
     * @param id the pitch to edit
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        PitchResponseDTO pitch = pitchService.getPitch(id);

        PitchRequestDTO dto = new PitchRequestDTO();
        dto.setName(pitch.getName());
        dto.setLocationId(pitch.getLocationId());
        dto.setCapacity(pitch.getCapacity());
        dto.setActive(pitch.getActive());

        model.addAttribute("pitchRequestDto", dto);
        addLocationAttributes(model);
        model.addAttribute("pitchId", id);
        return "pitch-form";
    }

    /**
     * Submits the edit-pitch form. If validation fails, it re-shows the form with errors.
     * @param id the pitch being updated
     * @param dto the submitted pitch details
     */
    @PostMapping("/{id}")
    public String updatePitch(@PathVariable Long id,
                               @Valid @ModelAttribute("pitchRequestDto") PitchRequestDTO dto,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            addLocationAttributes(model);
            model.addAttribute("pitchId", id);
            return "pitch-form";
        }
        try {
            pitchService.updatePitch(dto, id, dto.getActive());
            return "redirect:/admin/pitches?updateSuccess=true";
        } catch (ResponseStatusException e) {
            model.addAttribute("errorMessage", e.getReason());
            addLocationAttributes(model);
            model.addAttribute("pitchId", id);
            return "pitch-form";
        }
    }

    @PostMapping("/{id}/status")
    public String updatePitchStatus(@PathVariable Long id, @RequestParam boolean activeStatus) {
        pitchService.setActiveStatus(id, activeStatus);
        return "redirect:/admin/pitches#pitch-" + id;
    }

    /** Private helper to populates the model with location dropdown options shared by the create and edit forms.*/
    private void addLocationAttributes(Model model) {
        List<Location> locations = locationRepository.findAll();
        model.addAttribute("locations", locations);
        model.addAttribute("cities", locations.stream().map(Location::getCity).distinct().sorted().toList());
    }
}
