package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.response.SkillProfileResponseDTO;
import com.example.TalentOrbit.dto.response.StudentProfileDTO;
import com.example.TalentOrbit.entity.StudentDetails;
import com.example.TalentOrbit.entity.StudentSkill;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.StudentDetailsRepository;
import com.example.TalentOrbit.repository.StudentSkillRepository;
import com.example.TalentOrbit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    @Autowired private UserRepository userRepository;
    @Autowired private StudentDetailsRepository studentDetailsRepository;
    @Autowired private StudentSkillRepository studentSkillRepository;

    public StudentProfileDTO getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        StudentDetails details = studentDetailsRepository.findById(userId).orElse(new StudentDetails());
        StudentProfileDTO dto = new StudentProfileDTO();
        dto.setId(userId);
        dto.setEmail(user.getEmail());
        dto.setName(details.getName());
        dto.setInstitutionName(details.getInstitutionName());
        dto.setAisheCode(details.getAisheCode());
        dto.setBranch(details.getBranch());
        dto.setGradYear(details.getGradYear());
        dto.setCgpa(details.getCgpa());
        dto.setTargetRole(details.getTargetRole());
        dto.setEmployabilityScore(details.getEmployabilityScore());
        return dto;
    }

    public List<SkillProfileResponseDTO> getSkills(Long userId) {
        List<StudentSkill> list = studentSkillRepository.findByUserId(userId);
        return list.stream().map(ss -> {
            SkillProfileResponseDTO dto = new SkillProfileResponseDTO();
            dto.setSkillId(ss.getSkill().getId());
            dto.setSkillName(ss.getSkill().getName());
            dto.setCategory(ss.getSkill().getCategory());
            dto.setProficiency(ss.getProficiency());
            dto.setIsVerified(ss.getIsVerified());
            dto.setLastAssessed(ss.getLastAssessed());
            return dto;
        }).collect(Collectors.toList());
    }
}
