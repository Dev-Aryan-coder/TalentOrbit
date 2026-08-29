package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.RoadmapStepStatusUpdateDTO;
import com.example.TalentOrbit.dto.response.RoadmapStepResponseDTO;
import com.example.TalentOrbit.service.RoadmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roadmap")
public class RoadmapController {
    @Autowired private RoadmapService roadmapService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RoadmapStepResponseDTO>> getSteps(@PathVariable Long userId) {
        return ResponseEntity.ok(roadmapService.getStepsForUser(userId));
    }

    @PatchMapping("/step/{stepId}")
    public ResponseEntity<RoadmapStepResponseDTO> updateStep(
            @PathVariable Long stepId,
            @RequestBody RoadmapStepStatusUpdateDTO req) {
        return ResponseEntity.ok(roadmapService.updateStepStatus(stepId, req));
    }
}
