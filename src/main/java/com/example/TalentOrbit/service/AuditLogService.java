package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.response.AuditLogResponseDTO;
import com.example.TalentOrbit.entity.AuditLog;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditLogService {
    @Autowired private AuditLogRepository auditLogRepository;

    public void logAction(User actor, String actionType, String targetType, Long targetId, String ipAddress) {
        AuditLog log = new AuditLog(actor, actionType, targetType, targetId, ipAddress);
        auditLogRepository.save(log);
    }

    public List<AuditLogResponseDTO> getRecentLogs() {
        return auditLogRepository.findTop100ByOrderByTimestampDesc().stream().map(al -> {
            AuditLogResponseDTO dto = new AuditLogResponseDTO();
            dto.setId(al.getId());
            if (al.getActor() != null) dto.setActorEmail(al.getActor().getEmail());
            dto.setActionType(al.getActionType());
            dto.setTargetType(al.getTargetType());
            dto.setTargetId(al.getTargetId());
            dto.setIpAddress(al.getIpAddress());
            dto.setTimestamp(al.getTimestamp());
            return dto;
        }).collect(Collectors.toList());
    }
}
