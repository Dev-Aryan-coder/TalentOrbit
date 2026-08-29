package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.response.BadgeResponseDTO;
import com.example.TalentOrbit.service.BadgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/badges")
public class BadgeController {
    @Autowired private BadgeService badgeService;

    @GetMapping("/student/{userId}")
    public ResponseEntity<List<BadgeResponseDTO>> getBadges(@PathVariable Long userId) {
        return ResponseEntity.ok(badgeService.getBadgesForStudent(userId));
    }
}
