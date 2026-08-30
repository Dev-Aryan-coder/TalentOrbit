package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.ApplicationCreateDTO;
import com.example.TalentOrbit.dto.request.ApplicationStatusUpdateDTO;
import com.example.TalentOrbit.dto.response.ApplicationResponseDTO;
import com.example.TalentOrbit.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired private ApplicationService applicationService;

    @PostMapping("/apply")
    public ResponseEntity<ApplicationResponseDTO> apply(@Valid @RequestBody ApplicationCreateDTO req) {
        return ResponseEntity.ok(applicationService.apply(req));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ApplicationResponseDTO>> getUserApplications(@PathVariable Long userId) {
        return ResponseEntity.ok(applicationService.getUserApplications(userId));
    }

    @GetMapping("/posting/{postingId}/ranked")
    public ResponseEntity<List<ApplicationResponseDTO>> getRankedApplicants(@PathVariable Long postingId) {
        return ResponseEntity.ok(applicationService.getRankedApplicantsForPosting(postingId));
    }

    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<ApplicationResponseDTO> updateStatus(
            @PathVariable Long applicationId,
            @RequestBody ApplicationStatusUpdateDTO req) {
        return ResponseEntity.ok(applicationService.updateStatus(applicationId, req));
    }
}
