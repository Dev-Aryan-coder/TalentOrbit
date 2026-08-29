package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.ApplicationCreateDTO;
import com.example.TalentOrbit.dto.request.ApplicationStatusUpdateDTO;
import com.example.TalentOrbit.dto.response.ApplicationResponseDTO;
import com.example.TalentOrbit.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    @Autowired private ApplicationService applicationService;

    // 1-Click Apply
    @PostMapping("/apply")
    public ResponseEntity<ApplicationResponseDTO> apply(@RequestBody ApplicationCreateDTO req) {
        return ResponseEntity.ok(applicationService.apply(req));
    }

    // Student Application List
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ApplicationResponseDTO>> getUserApplications(@PathVariable Long userId) {
        return ResponseEntity.ok(applicationService.getUserApplications(userId));
    }

    // Recruiter Ranked Candidates for a Posting
    @GetMapping("/posting/{postingId}/ranked-candidates")
    public ResponseEntity<List<ApplicationResponseDTO>> getRankedCandidatesForPosting(@PathVariable Long postingId) {
        return ResponseEntity.ok(applicationService.getRankedApplicantsForPosting(postingId));
    }

    // Status / Mentor Feedback Update
    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<ApplicationResponseDTO> updateStatus(
            @PathVariable Long applicationId,
            @RequestBody ApplicationStatusUpdateDTO req) {
        return ResponseEntity.ok(applicationService.updateStatus(applicationId, req));
    }
}
