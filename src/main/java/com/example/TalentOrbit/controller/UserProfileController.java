package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.ChangePasswordRequestDTO;
import com.example.TalentOrbit.dto.request.UserProfileUpdateDTO;
import com.example.TalentOrbit.dto.response.UserProfileResponseDTO;
import com.example.TalentOrbit.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-profile")
public class UserProfileController {

    @Autowired private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponseDTO> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userProfileService.getProfile(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserProfileResponseDTO> updateProfile(
            @PathVariable Long userId,
            @RequestBody UserProfileUpdateDTO req) {
        return ResponseEntity.ok(userProfileService.updateProfile(userId, req));
    }

    @PutMapping("/{userId}/change-password")
    public ResponseEntity<String> changePassword(
            @PathVariable Long userId,
            @Valid @RequestBody ChangePasswordRequestDTO req) {
        userProfileService.changePassword(userId, req);
        return ResponseEntity.ok("Password updated successfully");
    }
}