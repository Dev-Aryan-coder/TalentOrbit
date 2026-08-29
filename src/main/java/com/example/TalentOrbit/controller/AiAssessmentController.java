package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.AiAssessmentRequestDTO;
import com.example.TalentOrbit.dto.response.AiAssessmentResponseDTO;
import com.example.TalentOrbit.dto.response.QuestionResponseDTO;
import com.example.TalentOrbit.service.AiAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessment")
public class AiAssessmentController {

    @Autowired private AiAssessmentService aiAssessmentService;

    @GetMapping("/questions/{skillId}")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionsForSkill(@PathVariable Long skillId) {
        return ResponseEntity.ok(aiAssessmentService.getQuestionsForSkill(skillId));
    }

    @PostMapping("/evaluate-with-ai")
    public ResponseEntity<AiAssessmentResponseDTO> evaluateWithAi(@RequestBody AiAssessmentRequestDTO req) {
        return ResponseEntity.ok(aiAssessmentService.evaluateAssessmentWithAi(req));
    }
}
