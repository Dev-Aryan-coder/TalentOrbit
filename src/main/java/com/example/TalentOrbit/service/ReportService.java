package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.ReportGenerateRequestDTO;
import com.example.TalentOrbit.dto.response.ReportResponseDTO;
import com.example.TalentOrbit.entity.Report;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.ReportRepository;
import com.example.TalentOrbit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {
    @Autowired private ReportRepository reportRepository;
    @Autowired private UserRepository userRepository;

    public ReportResponseDTO generateReport(ReportGenerateRequestDTO req) {
        User user = userRepository.findById(req.getGeneratedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Report r = new Report();
        r.setGeneratedBy(user);
        r.setReportType(req.getReportType());
        r.setDateRange(req.getDateRange());
        r.setFilePath("/reports/" + req.getReportType().name().toLowerCase() + "_report.pdf");
        Report saved = reportRepository.save(r);

        ReportResponseDTO dto = new ReportResponseDTO();
        dto.setId(saved.getId());
        dto.setReportType(saved.getReportType());
        dto.setDateRange(saved.getDateRange());
        dto.setFilePath(saved.getFilePath());
        dto.setGeneratedAt(saved.getGeneratedAt());
        return dto;
    }

    public List<ReportResponseDTO> getReports(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
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
