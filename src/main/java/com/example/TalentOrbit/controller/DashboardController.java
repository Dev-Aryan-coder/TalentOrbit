package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.response.DashboardStatsResponseDTO;
import com.example.TalentOrbit.dto.response.SkillGapRowDTO;
import com.example.TalentOrbit.enums.Role;
import com.example.TalentOrbit.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DashboardController {
    @Autowired private DashboardService dashboardService;

    @GetMapping("/dashboard/stats")
    public ResponseEntity<DashboardStatsResponseDTO> getStats(@RequestParam(defaultValue = "STUDENT") Role role) {
        return ResponseEntity.ok(dashboardService.getStatsForRole(role));
    }

    @GetMapping("/admin/skill-gaps")
    public ResponseEntity<List<SkillGapRowDTO>> getSkillGaps(@RequestParam(required = false) Long institutionId) {
        return ResponseEntity.ok(dashboardService.getSkillGapTable(institutionId));
    }
}
