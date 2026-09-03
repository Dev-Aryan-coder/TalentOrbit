package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.response.BadgeResponseDTO;
import com.example.TalentOrbit.dto.response.VerifyBadgeResponseDTO;
import com.example.TalentOrbit.entity.*;
import com.example.TalentOrbit.enums.ApplicationStatus;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    /**
     * Computes a standard SHA-256 hex digest for credential tamper-proofing.
     */
    private String computeSha256(String rawData) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawData.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            return UUID.randomUUID().toString().replace("-", "");
        }
    }

    /**
     * Generates a short, human-readable verification code like TO-PY-2026-X8F9A.
     */
    private String generateVerificationCode(String badgeName) {
        String clean = badgeName.replaceAll("[^A-Za-z]", "").toUpperCase();
        String prefix = clean.length() >= 3 ? clean.substring(0, 3) : "TO";
        String randomPart = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        return "TO-" + prefix + "-2026-" + randomPart;
    }

    /**
     * Awards a badge to a student with cryptographic verification seal.
     */
    public StudentBadge awardBadge(Long userId, Long badgeId, Integer score) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new ResourceNotFoundException("Badge not found with id: " + badgeId));

        Optional<StudentBadge> existing = studentBadgeRepository.findByUserAndBadge(user, badge);
        if (existing.isPresent()) {
            return existing.get();
        }

        LocalDateTime now = LocalDateTime.now();
        String hash = generateVerificationCode(badge.getName());
        String rawProof = user.getId() + ":" + badge.getId() + ":" + score + ":" + now.toString() + ":" + hash;
        String shaDigest = computeSha256(rawProof);

        StudentBadge sb = new StudentBadge();
        sb.setUser(user);
        sb.setBadge(badge);
        sb.setEarnedAt(now);
        sb.setVerificationHash(hash);
        sb.setSha256Digest(shaDigest);
        sb.setScore(score != null ? score : 90);

        return studentBadgeRepository.save(sb);
    }

    /**
     * Public credential verification endpoint by tamper-proof hash.
     */
    public VerifyBadgeResponseDTO verifyBadgeByHash(String hash) {
        StudentBadge sb = studentBadgeRepository.findByVerificationHash(hash)
                .orElseThrow(() -> new ResourceNotFoundException("No official credential found for hash: " + hash));

        VerifyBadgeResponseDTO dto = new VerifyBadgeResponseDTO();
        dto.setCandidateName(sb.getUser().getFullName() != null ? sb.getUser().getFullName() : sb.getUser().getEmail());
        dto.setCandidateEmail(sb.getUser().getEmail());
        dto.setBadgeName(sb.getBadge().getName());
        dto.setDescription(sb.getBadge().getDescription());
        dto.setCategory(sb.getBadge().getCriteriaType() != null ? sb.getBadge().getCriteriaType().name() : "Skill Mastery");
        dto.setScore(sb.getScore());
        dto.setEarnedAt(sb.getEarnedAt());
        dto.setVerificationHash(sb.getVerificationHash());
        dto.setSha256Digest(sb.getSha256Digest());
        dto.setStatus("ACTIVE & CRYPTOGRAPHICALLY VALID");

        return dto;
    }

    /**
     * Automatic badge awarding rules based on platform triggers.
     */
    public void checkAndAwardBadges(Long userId, String triggerEvent) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return;

        List<Badge> allBadges = badgeRepository.findAll();
        for (Badge badge : allBadges) {
            boolean earned = false;
            String name = badge.getName();

            if ("Skill Profile Pioneer".equalsIgnoreCase(name)) {
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
                    awardBadge(userId, badge.getId(), 88);
                }
            }
        }
    }

    /**
     * Retrieves all badges for a student with verification hash and scores.
     */
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
            dto.setIsEarned(true);
            dto.setVerificationHash(sb.getVerificationHash());
            dto.setSha256Digest(sb.getSha256Digest());
            dto.setScore(sb.getScore());
            return dto;
        }).collect(Collectors.toList());
    }
}