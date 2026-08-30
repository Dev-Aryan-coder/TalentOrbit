package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.StudentProfileUpdateDTO;
import com.example.TalentOrbit.dto.response.CareerSuggestionResponseDTO;
import com.example.TalentOrbit.dto.response.StudentProfileResponseDTO;
import com.example.TalentOrbit.service.RoadmapService;
import com.example.TalentOrbit.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    @Autowired private StudentService studentService;
    @Autowired private RoadmapService roadmapService;

    @GetMapping("/{userId}")
    public ResponseEntity<StudentProfileResponseDTO> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(studentService.getProfile(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<StudentProfileResponseDTO> updateProfile(
            @PathVariable Long userId,
            @RequestBody StudentProfileUpdateDTO req) {
        return ResponseEntity.ok(studentService.updateProfile(userId, req));
    }

    @GetMapping("/{userId}/career-suggestions")
    public ResponseEntity<List<CareerSuggestionResponseDTO>> getCareerSuggestions(@PathVariable Long userId) {
        return ResponseEntity.ok(roadmapService.getCareerSuggestions(userId));
    }
}
