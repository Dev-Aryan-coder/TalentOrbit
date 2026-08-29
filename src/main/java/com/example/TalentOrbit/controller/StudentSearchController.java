package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.StudentSearchFilterDTO;
import com.example.TalentOrbit.dto.request.TalentInviteRequestDTO;
import com.example.TalentOrbit.dto.response.RankedStudentResponseDTO;
import com.example.TalentOrbit.email.EmailService;
import com.example.TalentOrbit.entity.Posting;
import com.example.TalentOrbit.entity.StudentDetails;
import com.example.TalentOrbit.entity.StudentSkill;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.PostingRepository;
import com.example.TalentOrbit.repository.StudentDetailsRepository;
import com.example.TalentOrbit.repository.StudentSkillRepository;
import com.example.TalentOrbit.repository.UserRepository;
import com.example.TalentOrbit.service.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/talent-pool")
public class StudentSearchController {

    @Autowired private StudentDetailsRepository studentDetailsRepository;
    @Autowired private StudentSkillRepository studentSkillRepository;
    @Autowired private PostingRepository postingRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;
    @Autowired private AuditLogService auditLogService;

    @PostMapping("/search")
    public ResponseEntity<List<RankedStudentResponseDTO>> searchTalentPool(@RequestBody(required = false) StudentSearchFilterDTO filter) {
        List<StudentDetails> students = studentDetailsRepository.findAll();
        List<RankedStudentResponseDTO> results = new ArrayList<>();

        for (StudentDetails sd : students) {
            if (filter != null && filter.getBranch() != null && !filter.getBranch().trim().isEmpty()) {
                if (sd.getBranch() == null || !sd.getBranch().toLowerCase().contains(filter.getBranch().trim().toLowerCase())) {
                    continue;
                }
            }

            if (filter != null && filter.getMinCgpa() != null) {
                if (sd.getCgpa() == null || sd.getCgpa() < filter.getMinCgpa()) {
                    continue;
                }
            }

            if (filter != null && filter.getMinEmployabilityScore() != null) {
                if (sd.getEmployabilityScore() == null || sd.getEmployabilityScore() < filter.getMinEmployabilityScore()) {
                    continue;
                }
            }

            List<StudentSkill> studentSkills = studentSkillRepository.findByUserId(sd.getUser().getId());
            List<String> realSkills = studentSkills.stream()
                    .map(ss -> ss.getSkill().getName())
                    .collect(Collectors.toList());

            List<String> matched = new ArrayList<>();
            List<String> missing = new ArrayList<>();

            int computedScore = sd.getEmployabilityScore() != null ? sd.getEmployabilityScore() : 80;
            if (filter != null && filter.getRequiredSkills() != null && !filter.getRequiredSkills().isEmpty()) {
                Set<String> studentSkillSet = realSkills.stream()
                        .map(String::toLowerCase)
                        .collect(Collectors.toSet());

                for (String req : filter.getRequiredSkills()) {
                    if (studentSkillSet.contains(req.trim().toLowerCase())) {
                        matched.add(req);
                    } else {
                        missing.add(req);
                    }
                }
                computedScore = (int) Math.round(((double) matched.size() / filter.getRequiredSkills().size()) * 100.0);
            } else {
                matched.addAll(realSkills);
            }

            RankedStudentResponseDTO dto = new RankedStudentResponseDTO();
            dto.setUserId(sd.getUser().getId());
            dto.setName(sd.getName());
            dto.setInstitutionName(sd.getInstitutionName());
            dto.setBranch(sd.getBranch());
            dto.setCgpa(sd.getCgpa());
            dto.setMatchScore(computedScore);
            dto.setTopSkills(realSkills.isEmpty() ? Arrays.asList("Profile Setup Pending") : realSkills);
            dto.setMatchedSkills(matched);
            dto.setMissingSkills(missing);
            results.add(dto);
        }

        results.sort((a, b) -> (b.getMatchScore() != null && a.getMatchScore() != null)
                ? b.getMatchScore().compareTo(a.getMatchScore()) : 0);

        return ResponseEntity.ok(results);
    }

    @PostMapping("/invite")
    public ResponseEntity<String> inviteStudent(@RequestBody TalentInviteRequestDTO req, HttpServletRequest request) {
        User recruiter = userRepository.findById(req.getRecruiterUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter not found"));
        User student = userRepository.findById(req.getStudentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Posting posting = postingRepository.findById(req.getPostingId())
                .orElseThrow(() -> new ResourceNotFoundException("Posting not found"));

        emailService.sendTalentInvitationEmail(student.getEmail(), recruiter.getEmail(), posting.getTitle(), posting.getId());
        auditLogService.log(recruiter, "TALENT_INVITE_SENT", "STUDENT", student.getId(), request);

        return ResponseEntity.ok("Invitation successfully sent to " + student.getEmail());
    }
}
