package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.StudentProfileUpdateDTO;
import com.example.TalentOrbit.dto.response.StudentProfileResponseDTO;
import com.example.TalentOrbit.entity.StudentDetails;
import com.example.TalentOrbit.entity.StudentSkill;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.enums.ProficiencyLevel;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.SkillRepository;
import com.example.TalentOrbit.repository.StudentDetailsRepository;
import com.example.TalentOrbit.repository.StudentSkillRepository;
import com.example.TalentOrbit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired private StudentDetailsRepository studentDetailsRepository;
    @Autowired private StudentSkillRepository studentSkillRepository;
    @Autowired private SkillRepository skillRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private BadgeService badgeService;

    public StudentProfileResponseDTO getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        StudentDetails sd = studentDetailsRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Student details not found"));

        List<StudentSkill> skills = studentSkillRepository.findByUserId(userId);
        List<String> skillNames = skills.stream()
                .map(ss -> ss.getSkill().getName() + " (" + ss.getProficiency() + (ss.getIsVerified() ? " - Verified" : "") + ")")
                .collect(Collectors.toList());

        StudentProfileResponseDTO dto = new StudentProfileResponseDTO();
        dto.setId(sd.getId());
        dto.setUserId(user.getId());
        dto.setName(sd.getName());
        dto.setInstitutionName(sd.getInstitutionName());
        dto.setBranch(sd.getBranch());
        dto.setGradYear(sd.getGradYear());
        dto.setCgpa(sd.getCgpa());
        dto.setTargetRole(sd.getTargetRole());
        dto.setEmployabilityScore(sd.getEmployabilityScore());
        dto.setSkills(skillNames);
        return dto;
    }

    public StudentProfileResponseDTO updateProfile(Long userId, StudentProfileUpdateDTO req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        StudentDetails sd = studentDetailsRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Student details not found"));

        if (req.getName() != null) sd.setName(req.getName());
        if (req.getBranch() != null) sd.setBranch(req.getBranch());
        if (req.getGradYear() != null) sd.setGradYear(req.getGradYear());
        if (req.getCgpa() != null) sd.setCgpa(req.getCgpa());
        if (req.getTargetRole() != null) sd.setTargetRole(req.getTargetRole());
        studentDetailsRepository.save(sd);

        if (req.getSkills() != null) {
            for (String skillName : req.getSkills()) {
                skillRepository.findByName(skillName).ifPresent(skill -> {
                    Optional<StudentSkill> existingSkill = studentSkillRepository.findByUserAndSkill(user, skill);
                    StudentSkill studentSkill = existingSkill.orElse(new StudentSkill());
                    studentSkill.setUser(user);
                    studentSkill.setSkill(skill);
                    studentSkill.setProficiency(ProficiencyLevel.INTERMEDIATE);
                    studentSkillRepository.save(studentSkill);
                });
            }
        }

        // Trigger PROFILE_COMPLETE badge check if all fields filled
        if (sd.getName() != null && sd.getBranch() != null && sd.getGradYear() != null && sd.getCgpa() != null && sd.getTargetRole() != null) {
            badgeService.checkAndAwardBadges(user.getId(), "PROFILE_COMPLETE");
        }

        return getProfile(userId);
    }
}
