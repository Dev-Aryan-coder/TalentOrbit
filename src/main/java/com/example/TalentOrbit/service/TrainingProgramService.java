package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.TrainingProgramCreateDTO;
import com.example.TalentOrbit.dto.response.TrainingProgramResponseDTO;
import com.example.TalentOrbit.entity.Skill;
import com.example.TalentOrbit.entity.TrainingProgram;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.enums.TrainingProgramStatus;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.SkillRepository;
import com.example.TalentOrbit.repository.TrainingProgramRepository;
import com.example.TalentOrbit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingProgramService {
    @Autowired private TrainingProgramRepository programRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private SkillRepository skillRepository;

    public TrainingProgramResponseDTO createProgram(TrainingProgramCreateDTO req) {
        User user = userRepository.findById(req.getInstitutionUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Skill skill = (req.getTargetSkillId() != null) ? skillRepository.findById(req.getTargetSkillId()).orElse(null) : null;

        TrainingProgram p = new TrainingProgram();
        p.setInstitutionUser(user);
        p.setTitle(req.getTitle());
        p.setTargetSkill(skill);
        p.setProgramDate(req.getProgramDate());
        p.setStatus(TrainingProgramStatus.PLANNED);
        TrainingProgram saved = programRepository.save(p);

        TrainingProgramResponseDTO dto = new TrainingProgramResponseDTO();
        dto.setId(saved.getId());
        dto.setTitle(saved.getTitle());
        if (skill != null) dto.setTargetSkillName(skill.getName());
        dto.setProgramDate(saved.getProgramDate());
        dto.setStatus(saved.getStatus());
        return dto;
    }

    public List<TrainingProgramResponseDTO> getPrograms(Long institutionUserId) {
        User user = userRepository.findById(institutionUserId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return programRepository.findByInstitutionUser(user).stream().map(p -> {
            TrainingProgramResponseDTO dto = new TrainingProgramResponseDTO();
            dto.setId(p.getId());
            dto.setTitle(p.getTitle());
            if (p.getTargetSkill() != null) dto.setTargetSkillName(p.getTargetSkill().getName());
            dto.setProgramDate(p.getProgramDate());
            dto.setStudentsRegistered(p.getStudentsRegistered());
            dto.setStatus(p.getStatus());
            return dto;
        }).collect(Collectors.toList());
    }
}
