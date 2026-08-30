package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.RoadmapStepStatusUpdateDTO;
import com.example.TalentOrbit.dto.response.CareerSuggestionResponseDTO;
import com.example.TalentOrbit.dto.response.RoadmapStepResponseDTO;
import com.example.TalentOrbit.entity.*;
import com.example.TalentOrbit.enums.RoadmapStepStatus;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoadmapService {

    @Autowired private RoadmapStepRepository roadmapStepRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private StudentDetailsRepository studentDetailsRepository;
    @Autowired private StudentSkillRepository studentSkillRepository;
    @Autowired private RoleSkillTemplateRepository roleSkillTemplateRepository;
    @Autowired private RoleSkillTemplateSkillRepository roleSkillTemplateSkillRepository;

    public List<RoadmapStepResponseDTO> getStepsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<RoadmapStep> steps = roadmapStepRepository.findByUserOrderByStepOrderAsc(user);
        if (steps.isEmpty()) {
            steps = generateRoadmapForStudent(userId);
        }
        return steps.stream().map(s -> {
            RoadmapStepResponseDTO dto = new RoadmapStepResponseDTO();
            dto.setId(s.getId());
            dto.setStepOrder(s.getStepOrder());
            dto.setTitle(s.getTitle());
            dto.setDescription(s.getDescription());
            dto.setStatus(s.getStatus());
            dto.setLinkedSkillName(s.getLinkedSkill() != null ? s.getLinkedSkill().getName() : null);
            return dto;
        }).collect(Collectors.toList());
    }

    public List<RoadmapStep> generateRoadmapForStudent(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        StudentDetails sd = studentDetailsRepository.findByUser(user).orElse(null);
        String targetRole = (sd != null && sd.getTargetRole() != null) ? sd.getTargetRole() : "Backend Developer";

        Optional<RoleSkillTemplate> optTemplate = roleSkillTemplateRepository.findByRoleNameIgnoreCase(targetRole);
        if (optTemplate.isEmpty()) {
            optTemplate = roleSkillTemplateRepository.findAll().stream().findFirst();
        }

        if (optTemplate.isEmpty()) {
            return roadmapStepRepository.findByUserOrderByStepOrderAsc(user);
        }

        RoleSkillTemplate template = optTemplate.get();
        List<RoleSkillTemplateSkill> templateSkills = roleSkillTemplateSkillRepository.findByTemplate(template);
        templateSkills.sort((a, b) -> b.getWeight().compareTo(a.getWeight()));

        List<StudentSkill> studentSkills = studentSkillRepository.findByUserId(userId);
        Set<Long> heldSkillIds = studentSkills.stream().map(s -> s.getSkill().getId()).collect(Collectors.toSet());

        List<RoadmapStep> generatedSteps = new ArrayList<>();
        int order = 1;

        for (RoleSkillTemplateSkill tss : templateSkills) {
            RoadmapStep step = new RoadmapStep();
            step.setUser(user);
            step.setStepOrder(order++);
            step.setTitle("Master " + tss.getSkill().getName());
            step.setDescription("Core skill module required for " + template.getRoleName() + " (Priority Weight: " + tss.getWeight() + ")");
            step.setLinkedSkill(tss.getSkill());
            step.setStatus(heldSkillIds.contains(tss.getSkill().getId()) ? RoadmapStepStatus.DONE : (order == 2 ? RoadmapStepStatus.IN_PROGRESS : RoadmapStepStatus.LOCKED));
            generatedSteps.add(roadmapStepRepository.save(step));
        }

        return generatedSteps;
    }

    public List<CareerSuggestionResponseDTO> getCareerSuggestions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<StudentSkill> studentSkills = studentSkillRepository.findByUserId(userId);
        Set<String> heldSkillNames = studentSkills.stream()
                .map(s -> s.getSkill().getName().trim().toLowerCase())
                .collect(Collectors.toSet());

        List<RoleSkillTemplate> templates = roleSkillTemplateRepository.findAll();
        List<CareerSuggestionResponseDTO> suggestions = new ArrayList<>();

        for (RoleSkillTemplate template : templates) {
            List<RoleSkillTemplateSkill> reqSkills = roleSkillTemplateSkillRepository.findByTemplate(template);
            List<String> matched = new ArrayList<>();
            List<String> missing = new ArrayList<>();

            int totalWeight = 0;
            int matchedWeight = 0;

            for (RoleSkillTemplateSkill tss : reqSkills) {
                String skName = tss.getSkill().getName().trim();
                int weight = (tss.getWeight() != null && tss.getWeight() > 0) ? tss.getWeight() : 1;
                totalWeight += weight;

                if (heldSkillNames.contains(skName.toLowerCase())) {
                    matchedWeight += weight;
                    matched.add(skName);
                } else {
                    missing.add(skName);
                }
            }

            int fit = totalWeight > 0 ? (int) Math.round(((double) matchedWeight / totalWeight) * 100.0) : 0;
            suggestions.add(new CareerSuggestionResponseDTO(template.getRoleName(), template.getDescription(), fit, matched, missing));
        }

        suggestions.sort((a, b) -> Integer.compare(b.getFitPercent(), a.getFitPercent()));
        return suggestions;
    }

    public RoadmapStepResponseDTO updateStepStatus(Long stepId, RoadmapStepStatusUpdateDTO req) {
        RoadmapStep step = roadmapStepRepository.findById(stepId)
                .orElseThrow(() -> new ResourceNotFoundException("Roadmap step not found"));
        step.setStatus(req.getStatus());
        RoadmapStep saved = roadmapStepRepository.save(step);

        RoadmapStepResponseDTO dto = new RoadmapStepResponseDTO();
        dto.setId(saved.getId());
        dto.setStepOrder(saved.getStepOrder());
        dto.setTitle(saved.getTitle());
        dto.setDescription(saved.getDescription());
        dto.setStatus(saved.getStatus());
        dto.setLinkedSkillName(saved.getLinkedSkill() != null ? saved.getLinkedSkill().getName() : null);
        return dto;
    }
}
