package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.response.BadgeResponseDTO;
import com.example.TalentOrbit.entity.*;
import com.example.TalentOrbit.enums.ApplicationStatus;
import com.example.TalentOrbit.enums.BadgeCriteriaType;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BadgeService {

    @Autowired private BadgeRepository badgeRepository;
    @Autowired private StudentBadgeRepository studentBadgeRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ApplicationRepository applicationRepository;
    @Autowired private AssessmentRepository assessmentRepository;
    @Autowired private StudentSkillRepository studentSkillRepository;
    @Autowired private InterviewRepository interviewRepository;

    public void checkAndAwardBadges(Long userId, String triggerEvent) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return;

        List<Badge> allBadges = badgeRepository.findAll();
        for (Badge badge : allBadges) {
            boolean earned = false;
            String name = badge.getName();

            if ("Skill Profile Pioneer".equalsIgnoreCase(name)) {
                // Profile complete
                earned = true;
            } else if ("First Application".equalsIgnoreCase(name)) {
                earned = applicationRepository.findByUser(user).size() >= 1;
            } else if ("Assessment Master".equalsIgnoreCase(name)) {
                earned = assessmentRepository.findAll().stream().filter(a -> a.getUser().getId().equals(userId)).count() >= 5;
            } else if ("Skill Verified".equalsIgnoreCase(name)) {
                earned = studentSkillRepository.findByUserId(userId).stream().anyMatch(StudentSkill::getIsVerified);
            } else if ("Interview Ready".equalsIgnoreCase(name)) {
                earned = interviewRepository.findAll().stream().anyMatch(i -> i.getApplication().getUser().getId().equals(userId));
            } else if ("Placement Achiever".equalsIgnoreCase(name)) {
                earned = applicationRepository.findByUser(user).stream().anyMatch(a -> a.getStatus() == ApplicationStatus.COMPLETED || a.getStatus() == ApplicationStatus.SELECTED);
            }

            if (earned) {
                Optional<StudentBadge> existing = studentBadgeRepository.findByUserAndBadge(user, badge);
                if (existing.isEmpty()) {
                    StudentBadge sb = new StudentBadge();
                    sb.setUser(user);
                    sb.setBadge(badge);
                    sb.setEarnedAt(LocalDateTime.now());
                    studentBadgeRepository.save(sb);
                }
            }
        }
    }

    public List<BadgeResponseDTO> getBadgesForStudent(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return studentBadgeRepository.findByUser(user).stream().map(sb -> {
            BadgeResponseDTO dto = new BadgeResponseDTO();
            dto.setId(sb.getBadge().getId());
            dto.setName(sb.getBadge().getName());
            dto.setDescription(sb.getBadge().getDescription());
            dto.setIconUrl(sb.getBadge().getIconUrl());
            dto.setEarnedAt(sb.getEarnedAt());
            return dto;
        }).collect(Collectors.toList());
    }
}
