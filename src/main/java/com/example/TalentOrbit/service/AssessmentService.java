package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.AssessmentSubmissionDTO;
import com.example.TalentOrbit.entity.Assessment;
import com.example.TalentOrbit.entity.Skill;
import com.example.TalentOrbit.entity.StudentSkill;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.enums.ProficiencyLevel;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.AssessmentRepository;
import com.example.TalentOrbit.repository.SkillRepository;
import com.example.TalentOrbit.repository.StudentSkillRepository;
import com.example.TalentOrbit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AssessmentService {
    @Autowired private AssessmentRepository assessmentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private SkillRepository skillRepository;
    @Autowired private StudentSkillRepository studentSkillRepository;

    public void submitAssessment(AssessmentSubmissionDTO req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Skill skill = skillRepository.findById(req.getSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        Assessment a = new Assessment();
        a.setUser(user);
        a.setSkill(skill);
        a.setScore(req.getScore());
        a.setMaxScore(req.getMaxScore() != null ? req.getMaxScore() : 100);
        assessmentRepository.save(a);

        StudentSkill ss = studentSkillRepository.findByUserIdAndSkillId(user.getId(), skill.getId())
                .orElse(new StudentSkill());
        ss.setUser(user);
        ss.setSkill(skill);
        ss.setIsVerified(true);
        ss.setLastAssessed(LocalDate.now());

        if (req.getScore() >= 80) {
            ss.setProficiency(ProficiencyLevel.ADVANCED);
        } else if (req.getScore() >= 50) {
            ss.setProficiency(ProficiencyLevel.INTERMEDIATE);
        } else {
            ss.setProficiency(ProficiencyLevel.BEGINNER);
        }
        studentSkillRepository.save(ss);
    }
}
