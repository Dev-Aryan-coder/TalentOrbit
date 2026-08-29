package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.ReportGenerateRequestDTO;
import com.example.TalentOrbit.dto.response.ReportResponseDTO;
import com.example.TalentOrbit.entity.*;
import com.example.TalentOrbit.enums.ApplicationStatus;
import com.example.TalentOrbit.enums.ReportType;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {
    @Autowired private ReportRepository reportRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private StudentDetailsRepository studentDetailsRepository;
    @Autowired private ApplicationRepository applicationRepository;
    @Autowired private InstitutionDetailsRepository institutionDetailsRepository;

    public ReportResponseDTO generateReport(ReportGenerateRequestDTO req) {
        User user = userRepository.findById(req.getGeneratedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Report report = new Report();
        report.setGeneratedBy(user);
        report.setReportType(req.getReportType());
        report.setDateRange(req.getDateRange());
        report.setFilePath("/reports/TalentOrbit_Audit_" + System.currentTimeMillis() + ".pdf");
        Report saved = reportRepository.save(report);

        ReportResponseDTO dto = new ReportResponseDTO();
        dto.setId(saved.getId());
        dto.setReportType(saved.getReportType());
        dto.setDateRange(saved.getDateRange());
        dto.setFilePath(saved.getFilePath());
        dto.setGeneratedAt(saved.getGeneratedAt());
        return dto;
    }

    public ReportResponseDTO generateNirfFormatReport(Long institutionId) {
        InstitutionDetails inst = institutionDetailsRepository.findById(institutionId)
                .orElseThrow(() -> new ResourceNotFoundException("Institution not found"));

        List<StudentDetails> cohort = studentDetailsRepository.findByAisheCode(inst.getAisheCode());
        int totalStudents = cohort.size();

        long placedCount = applicationRepository.findAll().stream()
                .filter(a -> (a.getStatus() == ApplicationStatus.SELECTED || a.getStatus() == ApplicationStatus.COMPLETED))
                .filter(a -> cohort.stream().anyMatch(s -> s.getUser().getId().equals(a.getUser().getId())))
                .count();

        double placementRate = totalStudents > 0 ? ((double) placedCount / totalStudents) * 100.0 : 0.0;

        Report report = new Report();
        report.setGeneratedBy(inst.getUser());
        report.setReportType(ReportType.PLACEMENT_ANALYTICS);
        report.setDateRange("Academic Year 2025-2026");
        report.setFilePath(String.format(
            "/reports/NIRF_5.2.1_%s_Placed_%d_Rate_%.1fPct.pdf",
            inst.getAisheCode(), placedCount, placementRate
        ));
        Report saved = reportRepository.save(report);

        ReportResponseDTO dto = new ReportResponseDTO();
        dto.setId(saved.getId());
        dto.setReportType(saved.getReportType());
        dto.setDateRange("Placement summary formatted for NIRF Metric 5.2.1 / NAAC reference — verify figures independently before institutional submission");
        dto.setFilePath(saved.getFilePath());
        dto.setGeneratedAt(saved.getGeneratedAt());
        return dto;
    }

    public List<ReportResponseDTO> getReports(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return reportRepository.findByGeneratedBy(user).stream().map(r -> {
            ReportResponseDTO dto = new ReportResponseDTO();
            dto.setId(r.getId());
            dto.setReportType(r.getReportType());
            dto.setDateRange(r.getDateRange());
            dto.setFilePath(r.getFilePath());
            dto.setGeneratedAt(r.getGeneratedAt());
            return dto;
        }).collect(Collectors.toList());
    }
}
