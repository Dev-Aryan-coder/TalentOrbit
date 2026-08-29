package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.InterestTagUpdateDTO;
import com.example.TalentOrbit.dto.response.AcademicianInterestResponseDTO;
import com.example.TalentOrbit.dto.response.PostingResponseDTO;
import com.example.TalentOrbit.enums.PostingType;
import com.example.TalentOrbit.repository.PostingRepository;
import com.example.TalentOrbit.repository.PostingSkillRepository;
import com.example.TalentOrbit.service.AcademicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/academician")
public class AcademicianController {

    @Autowired private AcademicianService academicianService;
    @Autowired private PostingRepository postingRepository;
    @Autowired private PostingSkillRepository postingSkillRepository;

    @PostMapping("/express-interest")
    public ResponseEntity<String> expressInterest(@RequestParam Long userId, @RequestParam Long postingId) {
        academicianService.expressInterest(userId, postingId);
        return ResponseEntity.ok("Expressed interest successfully");
    }

    @GetMapping("/collaborations/{userId}")
    public ResponseEntity<List<AcademicianInterestResponseDTO>> getCollaborations(@PathVariable Long userId) {
        return ResponseEntity.ok(academicianService.getInterests(userId));
    }

    @PostMapping("/interests/update")
    public ResponseEntity<String> updateInterests(@RequestBody InterestTagUpdateDTO req) {
        academicianService.updateInterestTags(req);
        return ResponseEntity.ok("Expertise interest tags updated successfully");
    }

    @GetMapping("/{userId}/matched-research")
    public ResponseEntity<List<PostingResponseDTO>> getMatchedResearch(@PathVariable Long userId) {
        return ResponseEntity.ok(academicianService.getMatchedResearchOpportunities(userId));
    }

    @GetMapping("/opportunities/fdp")
    public ResponseEntity<List<PostingResponseDTO>> getFdps() {
        return ResponseEntity.ok(mapPostings(postingRepository.findByPostingTypeAndIsActiveTrue(PostingType.FDP)));
    }

    @GetMapping("/opportunities/research")
    public ResponseEntity<List<PostingResponseDTO>> getResearch() {
        return ResponseEntity.ok(mapPostings(postingRepository.findByPostingTypeAndIsActiveTrue(PostingType.RESEARCH)));
    }

    @GetMapping("/opportunities/consultancy")
    public ResponseEntity<List<PostingResponseDTO>> getConsultancies() {
        return ResponseEntity.ok(mapPostings(postingRepository.findByPostingTypeAndIsActiveTrue(PostingType.CONSULTANCY)));
    }

    @GetMapping("/opportunities/industry-training")
    public ResponseEntity<List<PostingResponseDTO>> getIndustryTrainings() {
        return ResponseEntity.ok(mapPostings(postingRepository.findByPostingTypeAndIsActiveTrue(PostingType.TRAINING)));
    }

    @GetMapping("/opportunities/workshops")
    public ResponseEntity<List<PostingResponseDTO>> getWorkshops() {
        return ResponseEntity.ok(mapPostings(postingRepository.findByPostingTypeAndIsActiveTrue(PostingType.WORKSHOP)));
    }

    private List<PostingResponseDTO> mapPostings(List<com.example.TalentOrbit.entity.Posting> list) {
        return list.stream().map(p -> {
            PostingResponseDTO dto = new PostingResponseDTO();
            dto.setId(p.getId());
            dto.setPostedByUserId(p.getPostedBy().getId());
            dto.setTitle(p.getTitle());
            dto.setPostingType(p.getPostingType());
            dto.setDescription(p.getDescription());
            dto.setLocation(p.getLocation());
            dto.setStipend(p.getStipend());
            dto.setDeadline(p.getDeadline());
            dto.setIsActive(p.getIsActive());
            dto.setRequiredSkills(postingSkillRepository.findByPosting(p).stream().map(ps -> ps.getSkill().getName()).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }
}
