package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.ScheduleTrainingDTO;
import com.example.TalentOrbit.dto.request.TrainingProgramCreateDTO;
import com.example.TalentOrbit.dto.response.TrainingProgramResponseDTO;
import com.example.TalentOrbit.entity.Skill;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.SkillRepository;
import com.example.TalentOrbit.service.TrainingProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TrainingProgramController {
    @Autowired private TrainingProgramService trainingProgramService;
    @Autowired private SkillRepository skillRepository;

    @PostMapping("/institution/training-programs/create")
    public ResponseEntity<TrainingProgramResponseDTO> create(@RequestBody TrainingProgramCreateDTO req) {
        return ResponseEntity.ok(trainingProgramService.createProgram(req));
    }

    @GetMapping("/institution/training-programs/institution/{institutionUserId}")
    public ResponseEntity<List<TrainingProgramResponseDTO>> getPrograms(@PathVariable Long institutionUserId) {
        return ResponseEntity.ok(trainingProgramService.getPrograms(institutionUserId));
    }

    @PostMapping("/admin/skill-gaps/{skillId}/schedule-training")
    public ResponseEntity<TrainingProgramResponseDTO> scheduleTrainingFromGap(
            @PathVariable Long skillId,
            @RequestBody ScheduleTrainingDTO req) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        TrainingProgramCreateDTO dto = new TrainingProgramCreateDTO();
        dto.setInstitutionUserId(req.getInstitutionId());
        dto.setTitle(skill.getName() + " Remedial Bootcamp");
        dto.setTargetSkillId(skill.getId());
        dto.setProgramDate(req.getScheduledDate());

        return ResponseEntity.ok(trainingProgramService.createProgram(dto));
    }
}
