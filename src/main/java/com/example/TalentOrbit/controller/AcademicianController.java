package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.InterestTagUpdateDTO;
import com.example.TalentOrbit.dto.response.AcademicianInterestResponseDTO;
import com.example.TalentOrbit.service.AcademicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academician")
public class AcademicianController {
    @Autowired private AcademicianService academicianService;

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
}
