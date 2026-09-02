package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.AiAssessmentRequestDTO;
import com.example.TalentOrbit.dto.response.AiAssessmentResponseDTO;
import com.example.TalentOrbit.dto.response.QuestionResponseDTO;
import com.example.TalentOrbit.enums.TechType;
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

    @GetMapping("/filter/tech-type/{techType}")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionsByTechType(@PathVariable String techType) {
        return ResponseEntity.ok(aiAssessmentService.getQuestionsByTechType(TechType.fromString(techType)));
    }

    @GetMapping("/filter/language/{language}")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionsByLanguage(@PathVariable String language) {
        return ResponseEntity.ok(aiAssessmentService.getQuestionsByLanguage(language));
    }

    @GetMapping("/filter/framework/{framework}")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionsByFramework(@PathVariable String framework) {
        return ResponseEntity.ok(aiAssessmentService.getQuestionsByFramework(framework));
    }

    @PostMapping("/evaluate-with-ai")
    public ResponseEntity<AiAssessmentResponseDTO> evaluateWithAi(@RequestBody AiAssessmentRequestDTO req) {
        return ResponseEntity.ok(aiAssessmentService.evaluateAssessmentWithAi(req));
    }
}
