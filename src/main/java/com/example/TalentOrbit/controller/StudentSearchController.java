package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.StudentSearchFilterDTO;
import com.example.TalentOrbit.dto.response.RankedStudentResponseDTO;
import com.example.TalentOrbit.entity.StudentDetails;
import com.example.TalentOrbit.entity.StudentSkill;
import com.example.TalentOrbit.repository.StudentDetailsRepository;
import com.example.TalentOrbit.repository.StudentSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/talent-pool")
public class StudentSearchController {
    @Autowired private StudentDetailsRepository studentDetailsRepository;
    @Autowired private StudentSkillRepository studentSkillRepository;

    @PostMapping("/search")
    public ResponseEntity<List<RankedStudentResponseDTO>> searchTalentPool(@RequestBody(required = false) StudentSearchFilterDTO filter) {
        List<StudentDetails> students = studentDetailsRepository.findAll();
        List<RankedStudentResponseDTO> results = new ArrayList<>();

        for (StudentDetails sd : students) {
            // Apply Branch filter
            if (filter != null && filter.getBranch() != null && !filter.getBranch().trim().isEmpty()) {
                if (sd.getBranch() == null || !sd.getBranch().toLowerCase().contains(filter.getBranch().trim().toLowerCase())) {
                    continue;
                }
            }

            // Apply Min CGPA filter
            if (filter != null && filter.getMinCgpa() != null) {
                if (sd.getCgpa() == null || sd.getCgpa() < filter.getMinCgpa()) {
                    continue;
                }
            }

            // Apply Min Employability Score filter
            if (filter != null && filter.getMinEmployabilityScore() != null) {
                if (sd.getEmployabilityScore() == null || sd.getEmployabilityScore() < filter.getMinEmployabilityScore()) {
                    continue;
                }
            }

            // Query REAL verified skills for this student from DB
            List<StudentSkill> studentSkills = studentSkillRepository.findByUserId(sd.getUser().getId());
            List<String> realSkills = studentSkills.stream()
                    .map(ss -> ss.getSkill().getName())
                    .collect(Collectors.toList());

            // If required skills filter provided, calculate dynamic match score against real skills
            int computedScore = sd.getEmployabilityScore() != null ? sd.getEmployabilityScore() : 80;
            if (filter != null && filter.getRequiredSkills() != null && !filter.getRequiredSkills().isEmpty()) {
                Set<String> studentSkillSet = realSkills.stream()
                        .map(String::toLowerCase)
                        .collect(Collectors.toSet());
                int matched = 0;
                for (String req : filter.getRequiredSkills()) {
                    if (studentSkillSet.contains(req.trim().toLowerCase())) {
                        matched++;
                    }
                }
                computedScore = (int) Math.round(((double) matched / filter.getRequiredSkills().size()) * 100.0);
            }

            RankedStudentResponseDTO dto = new RankedStudentResponseDTO();
            dto.setUserId(sd.getUser().getId());
            dto.setName(sd.getName());
            dto.setInstitutionName(sd.getInstitutionName());
            dto.setBranch(sd.getBranch());
            dto.setCgpa(sd.getCgpa());
            dto.setMatchScore(computedScore);
            dto.setTopSkills(realSkills.isEmpty() ? Arrays.asList("Profile Setup Pending") : realSkills);
            results.add(dto);
        }

        // Sort by match score descending
        results.sort((a, b) -> (b.getMatchScore() != null && a.getMatchScore() != null)
                ? b.getMatchScore().compareTo(a.getMatchScore()) : 0);

        return ResponseEntity.ok(results);
    }
}
