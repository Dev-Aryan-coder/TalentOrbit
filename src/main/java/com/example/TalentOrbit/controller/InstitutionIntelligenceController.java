package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.response.SkillDemandTrendDTO;
import com.example.TalentOrbit.dto.response.SkillGapRowDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/institution/intelligence")
public class InstitutionIntelligenceController {

    // 38_Institution_Skill_Intelligence (Demand vs Supply Heatmap)
    @GetMapping("/skill-heatmap/{aisheCode}")
    public ResponseEntity<List<SkillGapRowDTO>> getSkillHeatmap(@PathVariable String aisheCode) {
        List<SkillGapRowDTO> heatmap = new ArrayList<>();
        heatmap.add(new SkillGapRowDTO("Docker & Containerization", 84, 32, -52, 842));
        heatmap.add(new SkillGapRowDTO("AWS / Cloud DevOps", 78, 28, -50, 780));
        heatmap.add(new SkillGapRowDTO("Apache Kafka", 65, 22, -43, 620));
        heatmap.add(new SkillGapRowDTO("Spring Boot 3.3", 88, 74, -14, 210));
        heatmap.add(new SkillGapRowDTO("Java 21", 92, 86, -6, 85));
        return ResponseEntity.ok(heatmap);
    }

    // 42_Institution_Industry_Demand (Macro platform-wide trend)
    @GetMapping("/macro-industry-demand")
    public ResponseEntity<List<SkillDemandTrendDTO>> getMacroIndustryDemand() {
        List<SkillDemandTrendDTO> trends = new ArrayList<>();
        trends.add(new SkillDemandTrendDTO("Java 21 & Spring Boot", 92, "UP_14", 1420));
        trends.add(new SkillDemandTrendDTO("Docker & Kubernetes", 86, "UP_28", 1150));
        trends.add(new SkillDemandTrendDTO("React 19 & Next.js", 82, "UP_8", 980));
        trends.add(new SkillDemandTrendDTO("Ayush ABDM Standards", 74, "UP_35", 420));
        return ResponseEntity.ok(trends);
    }

    // 40_Institution_Placements (Salary Distribution & Offers Audit)
    @GetMapping("/placements-summary/{aisheCode}")
    public ResponseEntity<List<String>> getPlacementsSummary(@PathVariable String aisheCode) {
        List<String> summary = new ArrayList<>();
        summary.add("Total Offers: 320");
        summary.add("Placement Rate: 86.4%");
        summary.add("Average CTC: 8.6 LPA");
        summary.add("Highest CTC: 24.5 LPA");
        return ResponseEntity.ok(summary);
    }
}
