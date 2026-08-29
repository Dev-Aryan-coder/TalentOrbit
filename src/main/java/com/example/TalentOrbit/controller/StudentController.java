package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.AssessmentSubmissionDTO;
import com.example.TalentOrbit.dto.response.*;
import com.example.TalentOrbit.enums.PortfolioItemType;
import com.example.TalentOrbit.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    @Autowired private StudentService studentService;
    @Autowired private AssessmentService assessmentService;
    @Autowired private MatchingService matchingService;
    @Autowired private RoadmapService roadmapService;
    @Autowired private BadgeService badgeService;
    @Autowired private PortfolioService portfolioService;

    // 01_Student_Dashboard
    @GetMapping("/profile/{userId}")
    public ResponseEntity<StudentProfileDTO> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(studentService.getProfile(userId));
    }

    // 02_Student_Skill_Assessment
    @PostMapping("/assessment/submit")
    public ResponseEntity<String> submitAssessment(@RequestBody AssessmentSubmissionDTO req) {
        assessmentService.submitAssessment(req);
        return ResponseEntity.ok("Assessment submitted successfully and skill verified");
    }

    // 03_Student_My_Skills
    @GetMapping("/skills/{userId}")
    public ResponseEntity<List<SkillProfileResponseDTO>> getSkills(@PathVariable Long userId) {
        return ResponseEntity.ok(studentService.getSkills(userId));
    }

    // 04_Student_Career_Guidance & 06_Student_Opportunities
    @GetMapping("/matched-opportunities/{userId}")
    public ResponseEntity<List<PostingResponseDTO>> getMatchedOpportunities(@PathVariable Long userId) {
        return ResponseEntity.ok(matchingService.getMatchedPostingsForStudent(userId));
    }

    // 05_Student_Roadmap
    @GetMapping("/roadmap/{userId}")
    public ResponseEntity<List<RoadmapStepResponseDTO>> getRoadmap(@PathVariable Long userId) {
        return ResponseEntity.ok(roadmapService.getStepsForUser(userId));
    }

    // 08_Student_Portfolio (Projects)
    @GetMapping("/portfolio/projects/{userId}")
    public ResponseEntity<List<PortfolioItemResponseDTO>> getProjects(@PathVariable Long userId) {
        return ResponseEntity.ok(portfolioService.getItems(userId, PortfolioItemType.PROJECT));
    }

    // 09_Student_Achievements (Badges & Gamification)
    @GetMapping("/achievements/{userId}")
    public ResponseEntity<List<BadgeResponseDTO>> getAchievements(@PathVariable Long userId) {
        return ResponseEntity.ok(badgeService.getBadgesForStudent(userId));
    }

    // 10_Student_Certificates (Vault)
    @GetMapping("/certificates/{userId}")
    public ResponseEntity<List<PortfolioItemResponseDTO>> getCertificates(@PathVariable Long userId) {
        return ResponseEntity.ok(portfolioService.getItems(userId, PortfolioItemType.CERTIFICATE));
    }
}
