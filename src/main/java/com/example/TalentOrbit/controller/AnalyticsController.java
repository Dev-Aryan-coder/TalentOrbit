package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.response.DemandTrendDTO;
import com.example.TalentOrbit.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/superadmin/analytics")
public class AnalyticsController {

    @Autowired private AnalyticsService analyticsService;

    @GetMapping("/demand-trend")
    public ResponseEntity<Map<String, Object>> getDemandTrend(@RequestParam(defaultValue = "6") int months) {
        List<DemandTrendDTO> trends = analyticsService.getSkillDemandTrend(months);
        long activeCompanies = analyticsService.getTotalActiveCompanyCount();

        Map<String, Object> resp = new HashMap<>();
        resp.put("activeEmployerCount", activeCompanies);
        resp.put("monthsAnalyzed", months);
        resp.put("skillDemandTrends", trends);
        return ResponseEntity.ok(resp);
    }
}
