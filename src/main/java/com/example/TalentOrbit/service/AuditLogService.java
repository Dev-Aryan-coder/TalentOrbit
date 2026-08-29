package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.response.AuditLogResponseDTO;
import com.example.TalentOrbit.entity.AuditLog;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditLogService {
    @Autowired private AuditLogRepository auditLogRepository;

    public void log(User actor, String actionType, String targetType, Long targetId, String ipAddress) {
        AuditLog auditLog = new AuditLog(actor, actionType, targetType, targetId, ipAddress != null ? ipAddress : "127.0.0.1");
        auditLogRepository.save(auditLog);
    }

    public void log(User actor, String actionType, String targetType, Long targetId, HttpServletRequest request) {
        String ip = "127.0.0.1";
        if (request != null) {
            String forwarded = request.getHeader("X-Forwarded-For");
            ip = (forwarded != null && !forwarded.isEmpty()) ? forwarded.split(",")[0].trim() : request.getRemoteAddr();
        }
        log(actor, actionType, targetType, targetId, ip);
    }

    public List<AuditLogResponseDTO> getRecentLogs() {
        return auditLogRepository.findTop100ByOrderByTimestampDesc().stream().map(a -> {
            AuditLogResponseDTO dto = new AuditLogResponseDTO();
            dto.setId(a.getId());
            dto.setActorEmail(a.getActor() != null ? a.getActor().getEmail() : "SYSTEM");
            dto.setActionType(a.getActionType());
            dto.setTargetType(a.getTargetType());
            dto.setTargetId(a.getTargetId());
            dto.setIpAddress(a.getIpAddress());
            dto.setTimestamp(a.getTimestamp());
            return dto;
        }).collect(Collectors.toList());
    }
}
