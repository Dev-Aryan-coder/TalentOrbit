package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.ReportGenerateRequestDTO;
import com.example.TalentOrbit.dto.response.ReportResponseDTO;
import com.example.TalentOrbit.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    @Autowired private ReportService reportService;

    @PostMapping("/generate")
    public ResponseEntity<ReportResponseDTO> generateReport(@RequestBody ReportGenerateRequestDTO req) {
        return ResponseEntity.ok(reportService.generateReport(req));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReportResponseDTO>> getReports(@PathVariable Long userId) {
        return ResponseEntity.ok(reportService.getReports(userId));
    }
}
