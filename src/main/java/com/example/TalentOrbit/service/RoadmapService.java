package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.RoadmapStepStatusUpdateDTO;
import com.example.TalentOrbit.dto.response.RoadmapStepResponseDTO;
import com.example.TalentOrbit.entity.RoadmapStep;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.RoadmapStepRepository;
import com.example.TalentOrbit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoadmapService {
    @Autowired private RoadmapStepRepository roadmapStepRepository;
    @Autowired private UserRepository userRepository;

    public List<RoadmapStepResponseDTO> getStepsForUser(Long userId) {
        return roadmapStepRepository.findByUserIdOrderByStepOrderAsc(userId).stream().map(s -> {
            RoadmapStepResponseDTO dto = new RoadmapStepResponseDTO();
            dto.setId(s.getId());
            dto.setStepOrder(s.getStepOrder());
            dto.setTitle(s.getTitle());
            dto.setDescription(s.getDescription());
            dto.setStatus(s.getStatus());
            if (s.getLinkedSkill() != null) {
                dto.setLinkedSkillName(s.getLinkedSkill().getName());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    public RoadmapStepResponseDTO updateStepStatus(Long stepId, RoadmapStepStatusUpdateDTO req) {
        RoadmapStep step = roadmapStepRepository.findById(stepId)
                .orElseThrow(() -> new ResourceNotFoundException("Step not found"));
        step.setStatus(req.getStatus());
        RoadmapStep saved = roadmapStepRepository.save(step);
        RoadmapStepResponseDTO dto = new RoadmapStepResponseDTO();
        dto.setId(saved.getId());
        dto.setStepOrder(saved.getStepOrder());
        dto.setTitle(saved.getTitle());
        dto.setStatus(saved.getStatus());
        return dto;
    }
}
