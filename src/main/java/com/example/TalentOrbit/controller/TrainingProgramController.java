package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.TrainingProgramCreateDTO;
import com.example.TalentOrbit.dto.response.TrainingProgramResponseDTO;
import com.example.TalentOrbit.service.TrainingProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institution/training-programs")
public class TrainingProgramController {
    @Autowired private TrainingProgramService trainingProgramService;

    @PostMapping("/create")
    public ResponseEntity<TrainingProgramResponseDTO> create(@RequestBody TrainingProgramCreateDTO req) {
        return ResponseEntity.ok(trainingProgramService.createProgram(req));
    }

    @GetMapping("/institution/{institutionUserId}")
    public ResponseEntity<List<TrainingProgramResponseDTO>> getPrograms(@PathVariable Long institutionUserId) {
        return ResponseEntity.ok(trainingProgramService.getPrograms(institutionUserId));
    }
}
