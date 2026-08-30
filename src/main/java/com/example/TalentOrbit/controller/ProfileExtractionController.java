package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.FreeTextExtractionRequestDTO;
import com.example.TalentOrbit.dto.request.ProfileConfirmationRequestDTO;
import com.example.TalentOrbit.dto.response.ProfileExtractionResponseDTO;
import com.example.TalentOrbit.service.ProfileExtractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class ProfileExtractionController {

    @Autowired private ProfileExtractionService profileExtractionService;

    // AI Career Discovery: Step 1 - Extract structured profile from free text
    @PostMapping("/{id}/extract-profile")
    public ResponseEntity<ProfileExtractionResponseDTO> extractProfile(
            @PathVariable("id") Long userId,
            @RequestBody FreeTextExtractionRequestDTO req) {
        String text = (req != null) ? req.getFreeText() : "";
        return ResponseEntity.ok(profileExtractionService.extractProfile(userId, text));
    }

    // AI Career Discovery: Step 2 - Confirm extracted skills & career interest
    @PostMapping("/{id}/confirm-extracted-profile")
    public ResponseEntity<ProfileExtractionResponseDTO> confirmExtractedProfile(
            @PathVariable("id") Long userId,
            @RequestBody ProfileConfirmationRequestDTO req) {
        return ResponseEntity.ok(profileExtractionService.confirmProfile(userId, req));
    }
}
