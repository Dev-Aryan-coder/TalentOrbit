package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.response.BadgeResponseDTO;
import com.example.TalentOrbit.dto.response.VerifyBadgeResponseDTO;
import com.example.TalentOrbit.entity.StudentBadge;
import com.example.TalentOrbit.service.BadgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/badges")
@CrossOrigin(origins = "*")
public class BadgeController {

    @Autowired 
    private BadgeService badgeService;

    /**
     * Retrieve all earned badges for a student with verification hashes.
     */
    @GetMapping("/student/{userId}")
    public ResponseEntity<List<BadgeResponseDTO>> getBadges(@PathVariable Long userId) {
        return ResponseEntity.ok(badgeService.getBadgesForStudent(userId));
    }

    /**
     * Public tamper-proof credential verification lookup.
     * e.g., GET /api/badges/verify/TO-PY-2026-X8F9A
     */
    @GetMapping("/verify/{hash}")
    public ResponseEntity<VerifyBadgeResponseDTO> verifyCredential(@PathVariable String hash) {
        return ResponseEntity.ok(badgeService.verifyBadgeByHash(hash));
    }

    /**
     * Award a badge to a student upon passing an assessment.
     */
    @PostMapping("/award")
    public ResponseEntity<StudentBadge> awardBadge(
            @RequestParam Long userId,
            @RequestParam Long badgeId,
            @RequestParam(required = false, defaultValue = "88") Integer score) {
        return ResponseEntity.ok(badgeService.awardBadge(userId, badgeId, score));
    }
}