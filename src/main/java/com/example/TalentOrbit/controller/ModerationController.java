package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.FlagCreateDTO;
import com.example.TalentOrbit.dto.request.FlagDecisionDTO;
import com.example.TalentOrbit.dto.response.FlagResponseDTO;
import com.example.TalentOrbit.service.ModerationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moderation")
public class ModerationController {
    @Autowired private ModerationService moderationService;

    @PostMapping("/flag")
    public ResponseEntity<FlagResponseDTO> flagItem(@RequestBody FlagCreateDTO req) {
        return ResponseEntity.ok(moderationService.flagItem(req));
    }

    @GetMapping("/flags/pending")
    public ResponseEntity<List<FlagResponseDTO>> getPendingFlags() {
        return ResponseEntity.ok(moderationService.getPendingFlags());
    }

    @PatchMapping("/flag/{flagId}/decide")
    public ResponseEntity<String> decideFlag(
            @PathVariable Long flagId, 
            @RequestBody FlagDecisionDTO req,
            HttpServletRequest request) {
        moderationService.decideFlag(flagId, req, request);
        return ResponseEntity.ok("Flag decision updated");
    }
}
