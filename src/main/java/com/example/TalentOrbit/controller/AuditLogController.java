package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.response.AuditLogResponseDTO;
import com.example.TalentOrbit.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {
    @Autowired private AuditLogService auditLogService;

    @GetMapping("/recent")
    public ResponseEntity<List<AuditLogResponseDTO>> getRecentLogs() {
        return ResponseEntity.ok(auditLogService.getRecentLogs());
    }
}
