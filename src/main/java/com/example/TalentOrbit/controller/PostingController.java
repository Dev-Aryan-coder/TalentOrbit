package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.PostingCreateDTO;
import com.example.TalentOrbit.dto.request.SkillPreviewRequestDTO;
import com.example.TalentOrbit.dto.response.PostingResponseDTO;
import com.example.TalentOrbit.dto.response.SkillPreviewResponseDTO;
import com.example.TalentOrbit.enums.PostingType;
import com.example.TalentOrbit.service.PostingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/postings")
public class PostingController {

    @Autowired private PostingService postingService;

    @PostMapping("/preview-match-count")
    public ResponseEntity<SkillPreviewResponseDTO> previewMatchCount(@RequestBody SkillPreviewRequestDTO req) {
        return ResponseEntity.ok(postingService.previewMatchCount(req));
    }

    @PostMapping("/create")
    public ResponseEntity<PostingResponseDTO> createPosting(
            @Valid @RequestBody PostingCreateDTO req, 
            HttpServletRequest request) {
        return ResponseEntity.ok(postingService.createPosting(req, request));
    }

    @GetMapping("/active")
    public ResponseEntity<List<PostingResponseDTO>> getActivePostings() {
        return ResponseEntity.ok(postingService.getActivePostings());
    }

    @GetMapping("/fdps")
    public ResponseEntity<List<PostingResponseDTO>> getFDPs() {
        return ResponseEntity.ok(postingService.getPostingsByType(PostingType.FDP));
    }

    @GetMapping("/industry-trainings")
    public ResponseEntity<List<PostingResponseDTO>> getTrainings() {
        return ResponseEntity.ok(postingService.getPostingsByType(PostingType.INDUSTRY_TRAINING));
    }

    @GetMapping("/research-grants")
    public ResponseEntity<List<PostingResponseDTO>> getResearchProjects() {
        return ResponseEntity.ok(postingService.getPostingsByType(PostingType.RESEARCH));
    }

    @GetMapping("/consultancy-calls")
    public ResponseEntity<List<PostingResponseDTO>> getConsultancies() {
        return ResponseEntity.ok(postingService.getPostingsByType(PostingType.CONSULTANCY));
    }

    @GetMapping("/workshops")
    public ResponseEntity<List<PostingResponseDTO>> getWorkshops() {
        return ResponseEntity.ok(postingService.getPostingsByType(PostingType.WORKSHOP));
    }
}
