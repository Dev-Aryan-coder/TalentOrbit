package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.FlagCreateDTO;
import com.example.TalentOrbit.dto.request.FlagDecisionDTO;
import com.example.TalentOrbit.dto.response.FlagResponseDTO;
import com.example.TalentOrbit.entity.Flag;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.enums.FlagStatus;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.FlagRepository;
import com.example.TalentOrbit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModerationService {
    @Autowired private FlagRepository flagRepository;
    @Autowired private UserRepository userRepository;

    public FlagResponseDTO flagItem(FlagCreateDTO req) {
        User user = (req.getReportedByUserId() != null) ? userRepository.findById(req.getReportedByUserId()).orElse(null) : null;
        Flag flag = new Flag();
        flag.setReportedBy(user);
        flag.setItemType(req.getItemType());
        flag.setItemId(req.getItemId());
        flag.setReason(req.getReason());
        flag.setStatus(FlagStatus.PENDING);
        Flag saved = flagRepository.save(flag);

        FlagResponseDTO dto = new FlagResponseDTO();
        dto.setId(saved.getId());
        dto.setItemType(saved.getItemType());
        dto.setItemId(saved.getItemId());
        dto.setReason(saved.getReason());
        dto.setStatus(saved.getStatus());
        dto.setCreatedAt(saved.getCreatedAt());
        return dto;
    }

    public List<FlagResponseDTO> getPendingFlags() {
        return flagRepository.findByStatus(FlagStatus.PENDING).stream().map(f -> {
            FlagResponseDTO dto = new FlagResponseDTO();
            dto.setId(f.getId());
            dto.setItemType(f.getItemType());
            dto.setItemId(f.getItemId());
            dto.setReason(f.getReason());
            dto.setStatus(f.getStatus());
            dto.setCreatedAt(f.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    public void decideFlag(Long flagId, FlagDecisionDTO req) {
        Flag flag = flagRepository.findById(flagId)
                .orElseThrow(() -> new ResourceNotFoundException("Flag not found"));
        flag.setStatus(req.getStatus());
        flagRepository.save(flag);
    }
}
