package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.InterviewCreateDTO;
import com.example.TalentOrbit.dto.request.InterviewStatusUpdateDTO;
import com.example.TalentOrbit.dto.response.InterviewResponseDTO;
import com.example.TalentOrbit.service.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {
    @Autowired private InterviewService interviewService;

    @PostMapping("/schedule")
    public ResponseEntity<InterviewResponseDTO> schedule(@RequestBody InterviewCreateDTO req) {
        return ResponseEntity.ok(interviewService.scheduleInterview(req));
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<InterviewResponseDTO>> getByApplication(@PathVariable Long applicationId) {
        return ResponseEntity.ok(interviewService.getInterviewsForApplication(applicationId));
    }

    @PatchMapping("/{interviewId}/status")
    public ResponseEntity<InterviewResponseDTO> updateStatus(
            @PathVariable Long interviewId,
            @RequestBody InterviewStatusUpdateDTO req) {
        return ResponseEntity.ok(interviewService.updateStatus(interviewId, req));
    }
}
