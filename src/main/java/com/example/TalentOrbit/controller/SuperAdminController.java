package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.VerificationDecisionDTO;
import com.example.TalentOrbit.dto.response.PendingRegistrationResponseDTO;
import com.example.TalentOrbit.dto.response.UserSummaryResponseDTO;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.enums.Role;
import com.example.TalentOrbit.enums.UserStatus;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.UserRepository;
import com.example.TalentOrbit.service.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class SuperAdminController {
    @Autowired private SuperAdminService superAdminService;
    @Autowired private UserRepository userRepository;

    // 47, 48, 49_SuperAdmin Approvals Queue
    @GetMapping("/verifications/pending")
    public ResponseEntity<List<PendingRegistrationResponseDTO>> getPending() {
        return ResponseEntity.ok(superAdminService.getPendingVerifications());
    }

    // Approve / Reject Decision Trigger
    @PostMapping("/verifications/decide")
    public ResponseEntity<String> decide(@RequestBody VerificationDecisionDTO req) {
        superAdminService.decideVerification(req);
        return ResponseEntity.ok("Verification decision processed successfully");
    }

    // 46_SuperAdmin_Users Master List
    @GetMapping("/users")
    public ResponseEntity<List<UserSummaryResponseDTO>> getAllUsers(@RequestParam(required = false) Role role) {
        return ResponseEntity.ok(superAdminService.getAllUsers(role));
    }

    // User Suspend / Reactivate Action
    @PatchMapping("/user/{userId}/toggle-suspend")
    public ResponseEntity<String> toggleSuspend(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getStatus() == UserStatus.SUSPENDED) {
            user.setStatus(UserStatus.VERIFIED);
        } else {
            user.setStatus(UserStatus.SUSPENDED);
        }
        userRepository.save(user);
        return ResponseEntity.ok("User status updated to: " + user.getStatus());
    }
}
