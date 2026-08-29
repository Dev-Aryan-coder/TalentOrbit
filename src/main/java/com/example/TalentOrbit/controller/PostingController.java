package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.PostingCreateDTO;
import com.example.TalentOrbit.dto.response.PostingResponseDTO;
import com.example.TalentOrbit.enums.PostingType;
import com.example.TalentOrbit.service.PostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/postings")
public class PostingController {
    @Autowired private PostingService postingService;

    // 16_Industry_Post_Opportunity
    @PostMapping("/create")
    public ResponseEntity<PostingResponseDTO> createPosting(@RequestBody PostingCreateDTO req) {
        return ResponseEntity.ok(postingService.createPosting(req));
    }

    // 17_Industry_My_Opportunities & 26_Academician_Opportunities (All Active)
    @GetMapping("/active")
    public ResponseEntity<List<PostingResponseDTO>> getActivePostings() {
        return ResponseEntity.ok(postingService.getAllActivePostings());
    }

    // 27_Academician_FDP_Programs
    @GetMapping("/fdps")
    public ResponseEntity<List<PostingResponseDTO>> getFDPs() {
        List<PostingResponseDTO> all = postingService.getAllActivePostings();
        return ResponseEntity.ok(all.stream().filter(p -> p.getPostingType() == PostingType.FDP).collect(Collectors.toList()));
    }

    // 28_Academician_Industry_Training (Sabbaticals)
    @GetMapping("/industry-trainings")
    public ResponseEntity<List<PostingResponseDTO>> getTrainings() {
        List<PostingResponseDTO> all = postingService.getAllActivePostings();
        return ResponseEntity.ok(all.stream().filter(p -> p.getPostingType() == PostingType.TRAINING).collect(Collectors.toList()));
    }

    // 29_Academician_Research_Projects
    @GetMapping("/research-grants")
    public ResponseEntity<List<PostingResponseDTO>> getResearchProjects() {
        List<PostingResponseDTO> all = postingService.getAllActivePostings();
        return ResponseEntity.ok(all.stream().filter(p -> p.getPostingType() == PostingType.RESEARCH).collect(Collectors.toList()));
    }

    // 30_Academician_Consultancy
    @GetMapping("/consultancy-calls")
    public ResponseEntity<List<PostingResponseDTO>> getConsultancies() {
        List<PostingResponseDTO> all = postingService.getAllActivePostings();
        return ResponseEntity.ok(all.stream().filter(p -> p.getPostingType() == PostingType.CONSULTANCY).collect(Collectors.toList()));
    }

    // 31_Academician_Workshops
    @GetMapping("/workshops")
    public ResponseEntity<List<PostingResponseDTO>> getWorkshops() {
        List<PostingResponseDTO> all = postingService.getAllActivePostings();
        return ResponseEntity.ok(all.stream().filter(p -> p.getPostingType() == PostingType.WORKSHOP).collect(Collectors.toList()));
    }
}
