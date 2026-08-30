package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.FlagDecisionDTO;
import com.example.TalentOrbit.dto.response.FlagResponseDTO;
import com.example.TalentOrbit.entity.Flag;
import com.example.TalentOrbit.entity.Posting;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.enums.FlagItemType;
import com.example.TalentOrbit.enums.FlagStatus;
import com.example.TalentOrbit.enums.Role;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.FlagRepository;
import com.example.TalentOrbit.repository.PostingRepository;
import com.example.TalentOrbit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModerationService {

    @Autowired private FlagRepository flagRepository;
    @Autowired private PostingRepository postingRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private AuditLogService auditLogService;

    public List<FlagResponseDTO> getPendingFlags() {
        return flagRepository.findByStatus(FlagStatus.PENDING).stream().map(f -> {
            FlagResponseDTO dto = new FlagResponseDTO();
            dto.setId(f.getId());
            dto.setItemType(f.getItemType());
            dto.setItemId(f.getItemId());
            dto.setReportedByEmail(f.getReportedBy().getEmail());
            dto.setReason(f.getReason());
            dto.setStatus(f.getStatus());
            dto.setCreatedAt(f.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    public void decideFlag(FlagDecisionDTO req) {
        Flag flag = flagRepository.findById(req.getFlagId())
                .orElseThrow(() -> new ResourceNotFoundException("Flag not found"));
        flag.setStatus(req.getDecision());
        flagRepository.save(flag);

        // Deactivate underlying content if decision is REMOVED
        if (req.getDecision() == FlagStatus.REMOVED && flag.getItemType() == FlagItemType.POSTING) {
            Posting posting = postingRepository.findById(flag.getItemId()).orElse(null);
            if (posting != null) {
                posting.setIsActive(false);
                postingRepository.save(posting);
            }
        }

        User admin = userRepository.findAll().stream().filter(u -> u.getRole() == Role.SUPERADMIN).findFirst().orElse(null);
        if (admin != null) {
            auditLogService.log(admin, "MODERATION_FLAG_" + req.getDecision(), "FLAG", flag.getId(), "127.0.0.1");
        }
    }
}
