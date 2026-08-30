package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.response.PlacementsSummaryResponseDTO;
import com.example.TalentOrbit.dto.response.SkillDemandTrendDTO;
import com.example.TalentOrbit.dto.response.SkillGapRowDTO;
import com.example.TalentOrbit.entity.Application;
import com.example.TalentOrbit.entity.InstitutionDetails;
import com.example.TalentOrbit.entity.StudentDetails;
import com.example.TalentOrbit.enums.ApplicationStatus;
import com.example.TalentOrbit.repository.ApplicationRepository;
import com.example.TalentOrbit.repository.InstitutionDetailsRepository;
import com.example.TalentOrbit.repository.StudentDetailsRepository;
import com.example.TalentOrbit.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/institution/intelligence")
public class InstitutionIntelligenceController {

    @Autowired private InstitutionDetailsRepository institutionDetailsRepository;
    @Autowired private StudentDetailsRepository studentDetailsRepository;
    @Autowired private ApplicationRepository applicationRepository;
    @Autowired private DashboardService dashboardService;

    // 38_Institution_Skill_Intelligence (Real Demand vs Supply Heatmap)
    @GetMapping("/skill-heatmap/{aisheCode}")
    public ResponseEntity<List<SkillGapRowDTO>> getSkillHeatmap(@PathVariable String aisheCode) {
        InstitutionDetails inst = institutionDetailsRepository.findAll().stream()
                .filter(i -> aisheCode != null && aisheCode.equalsIgnoreCase(i.getAisheCode()))
                .findFirst()
                .orElse(null);

        Long institutionId = inst != null ? inst.getId() : null;
        return ResponseEntity.ok(dashboardService.getSkillGapTable(institutionId));
    }

    // 42_Institution_Industry_Demand (Macro platform-wide demand analytics)
    @GetMapping("/macro-industry-demand")
    public ResponseEntity<List<SkillDemandTrendDTO>> getMacroIndustryDemand() {
        List<SkillGapRowDTO> platformGaps = dashboardService.getSkillGapTable(null);
        List<SkillDemandTrendDTO> trends = new ArrayList<>();
        for (SkillGapRowDTO gap : platformGaps) {
            String growth = gap.getDemandPercentage() >= 50 ? "UP_24" : "UP_12";
            trends.add(new SkillDemandTrendDTO(gap.getSkillName(), gap.getDemandPercentage(), growth, gap.getAffectedStudents()));
        }
        return ResponseEntity.ok(trends);
    }

    // 40_Institution_Placements (Salary Distribution & Offers Audit - Real Computed Data)
    @GetMapping("/placements-summary/{aisheCode}")
    public ResponseEntity<PlacementsSummaryResponseDTO> getPlacementsSummary(@PathVariable String aisheCode) {
        List<StudentDetails> cohort = (aisheCode != null && !aisheCode.trim().isEmpty())
                ? studentDetailsRepository.findByAisheCode(aisheCode.trim())
                : Collections.emptyList();

        if (cohort.isEmpty()) {
            return ResponseEntity.ok(new PlacementsSummaryResponseDTO(0, 0.0, BigDecimal.ZERO, BigDecimal.ZERO));
        }

        Set<Long> cohortUserIds = cohort.stream()
                .map(s -> s.getUser().getId())
                .collect(Collectors.toSet());

        List<Application> placedApplications = applicationRepository.findAll().stream()
                .filter(a -> cohortUserIds.contains(a.getUser().getId()))
                .filter(a -> a.getStatus() == ApplicationStatus.SELECTED || a.getStatus() == ApplicationStatus.COMPLETED)
                .collect(Collectors.toList());

        int totalOffers = placedApplications.size();
        double placementRate = Math.round(((double) totalOffers / cohort.size()) * 1000.0) / 10.0;

        List<BigDecimal> packages = placedApplications.stream()
                .map(Application::getOfferedPackage)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        BigDecimal averageCtc = BigDecimal.ZERO;
        BigDecimal highestCtc = BigDecimal.ZERO;

        if (!packages.isEmpty()) {
            highestCtc = packages.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
            BigDecimal sum = packages.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            averageCtc = sum.divide(BigDecimal.valueOf(packages.size()), 2, RoundingMode.HALF_UP);
        }

        PlacementsSummaryResponseDTO response = new PlacementsSummaryResponseDTO(
                totalOffers,
                placementRate,
                averageCtc,
                highestCtc
        );

        return ResponseEntity.ok(response);
    }
}
