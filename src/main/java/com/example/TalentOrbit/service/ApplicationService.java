package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.ApplicationCreateDTO;
import com.example.TalentOrbit.dto.request.ApplicationStatusUpdateDTO;
import com.example.TalentOrbit.dto.response.ApplicationResponseDTO;
import com.example.TalentOrbit.email.EmailService;
import com.example.TalentOrbit.entity.*;
import com.example.TalentOrbit.enums.ApplicationStatus;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {
    @Autowired private ApplicationRepository applicationRepository;
    @Autowired private PostingRepository postingRepository;
    @Autowired private PostingSkillRepository postingSkillRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private StudentDetailsRepository studentDetailsRepository;
    @Autowired private StudentSkillRepository studentSkillRepository;
    @Autowired private MatchingService matchingService;
    @Autowired private BadgeService badgeService;
    @Autowired private EmailService emailService;

    @Value("${talentorbit.secret.key:TalentOrbitSecureSecretSalt2026}")
    private String serverSecret;

    public ApplicationResponseDTO apply(ApplicationCreateDTO req) {
        Posting posting = postingRepository.findById(req.getPostingId())
                .orElseThrow(() -> new ResourceNotFoundException("Posting not found"));
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        MatchingService.MatchResult matchResult = matchingService.calculateMatch(user.getId(), posting);

        Application app = new Application();
        app.setPosting(posting);
        app.setUser(user);
        app.setStatus(ApplicationStatus.APPLIED);
        app.setMatchScore(matchResult.score);
        Application saved = applicationRepository.save(app);

        badgeService.checkAndAwardBadges(user.getId(), "FIRST_APPLICATION");

        ApplicationResponseDTO dto = new ApplicationResponseDTO();
        dto.setId(saved.getId());
        dto.setPostingId(posting.getId());
        dto.setPostingTitle(posting.getTitle());
        dto.setUserId(user.getId());
        dto.setAppliedAt(saved.getAppliedAt());
        dto.setStatus(saved.getStatus());
        dto.setMatchScore(saved.getMatchScore());
        dto.setMatchedSkills(matchResult.matchedSkills);
        dto.setMissingSkills(matchResult.missingSkills);
        return dto;
    }

    public List<ApplicationResponseDTO> getUserApplications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return applicationRepository.findByUser(user).stream().map(a -> {
            ApplicationResponseDTO dto = new ApplicationResponseDTO();
            dto.setId(a.getId());
            dto.setPostingId(a.getPosting().getId());
            dto.setPostingTitle(a.getPosting().getTitle());
            dto.setUserId(a.getUser().getId());
            dto.setAppliedAt(a.getAppliedAt());
            dto.setStatus(a.getStatus());
            dto.setMatchScore(a.getMatchScore());
            dto.setMentorRating(a.getMentorRating());
            dto.setMentorFeedback(a.getMentorFeedback());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<ApplicationResponseDTO> getRankedApplicantsForPosting(Long postingId) {
        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new ResourceNotFoundException("Posting not found"));
        List<Application> apps = applicationRepository.findByPosting(posting);

        List<ApplicationResponseDTO> dtos = new ArrayList<>();
        for (Application a : apps) {
            MatchingService.MatchResult match = matchingService.calculateMatch(a.getUser().getId(), posting);
            StudentDetails sd = studentDetailsRepository.findByUser(a.getUser()).orElse(null);

            ApplicationResponseDTO dto = new ApplicationResponseDTO();
            dto.setId(a.getId());
            dto.setPostingId(posting.getId());
            dto.setPostingTitle(posting.getTitle());
            dto.setUserId(a.getUser().getId());
            dto.setStudentName(sd != null ? sd.getName() : a.getUser().getEmail());
            dto.setAppliedAt(a.getAppliedAt());
            dto.setStatus(a.getStatus());
            dto.setMatchScore(a.getMatchScore() != null ? a.getMatchScore() : match.score);
            dto.setMatchedSkills(match.matchedSkills);
            dto.setMissingSkills(match.missingSkills);
            dto.setMentorRating(a.getMentorRating());
            dto.setMentorFeedback(a.getMentorFeedback());
            dtos.add(dto);
        }

        dtos.sort((a, b) -> (b.getMatchScore() != null && a.getMatchScore() != null)
                ? b.getMatchScore().compareTo(a.getMatchScore()) : 0);
        return dtos;
    }

    public ApplicationResponseDTO updateStatus(Long applicationId, ApplicationStatusUpdateDTO req) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        if (req.getStatus() != null) app.setStatus(req.getStatus());
        if (req.getMentorRating() != null) app.setMentorRating(req.getMentorRating());
        if (req.getMentorFeedback() != null) app.setMentorFeedback(req.getMentorFeedback());
        Application saved = applicationRepository.save(app);

        // Closing the Academic-Industry Loop: If COMPLETED and mentorRating >= 4.0, verify student's skills with SHA-256 hash
        if (saved.getStatus() == ApplicationStatus.COMPLETED && saved.getMentorRating() != null && saved.getMentorRating() >= 4.0) {
            List<PostingSkill> requiredSkills = postingSkillRepository.findByPosting(saved.getPosting());
            for (PostingSkill ps : requiredSkills) {
                StudentSkill ss = studentSkillRepository.findByUserAndSkill(saved.getUser(), ps.getSkill())
                        .orElse(new StudentSkill());
                ss.setUser(saved.getUser());
                ss.setSkill(ps.getSkill());
                ss.setIsVerified(true);
                ss.setLastAssessed(LocalDate.now());

                // Compute tamper-evident SHA-256 hash
                String raw = saved.getUser().getId() + ":" + ps.getSkill().getId() + ":" + saved.getMentorRating() + ":" + System.currentTimeMillis() + ":" + serverSecret;
                ss.setVerificationHash(computeSha256(raw));
                studentSkillRepository.save(ss);
            }
            badgeService.checkAndAwardBadges(saved.getUser().getId(), "MENTOR_VERIFIED");
        }

        emailService.sendApplicationStatusEmail(app.getUser().getEmail(), "Student", app.getPosting().getTitle(), saved.getStatus().name());

        ApplicationResponseDTO dto = new ApplicationResponseDTO();
        dto.setId(saved.getId());
        dto.setPostingId(saved.getPosting().getId());
        dto.setPostingTitle(saved.getPosting().getTitle());
        dto.setUserId(saved.getUser().getId());
        dto.setStatus(saved.getStatus());
        dto.setMatchScore(saved.getMatchScore());
        dto.setMentorRating(saved.getMentorRating());
        dto.setMentorFeedback(saved.getMentorFeedback());
        return dto;
    }

    private String computeSha256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return "0x" + Integer.toHexString(data.hashCode());
        }
    }
}
