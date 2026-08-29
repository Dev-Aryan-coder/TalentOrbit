package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.VerificationDecisionDTO;
import com.example.TalentOrbit.dto.response.PendingRegistrationResponseDTO;
import com.example.TalentOrbit.dto.response.UserSummaryResponseDTO;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.enums.Role;
import com.example.TalentOrbit.enums.UserStatus;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.UserRepository;
import com.example.TalentOrbit.service.AuditLogService;
import com.example.TalentOrbit.service.SuperAdminService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class SuperAdminController {
    @Autowired private SuperAdminService superAdminService;
    @Autowired private UserRepository userRepository;
    @Autowired private AuditLogService auditLogService;

    @GetMapping("/verifications/pending")
    public ResponseEntity<List<PendingRegistrationResponseDTO>> getPending() {
        return ResponseEntity.ok(superAdminService.getPendingVerifications());
    }

    @PostMapping("/verifications/decide")
    public ResponseEntity<String> decide(@RequestBody VerificationDecisionDTO req, HttpServletRequest request) {
        superAdminService.decideVerification(req);
        User admin = userRepository.findAll().stream().filter(u -> u.getRole() == Role.SUPERADMIN).findFirst().orElse(null);
        if (admin != null) {
            auditLogService.log(admin, "KYC_DECISION_" + req.getDecision(), "USER", req.getUserId(), request);
        }
        return ResponseEntity.ok("Verification decision processed successfully");
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserSummaryResponseDTO>> getAllUsers(@RequestParam(required = false) Role role) {
        return ResponseEntity.ok(superAdminService.getAllUsers(role));
    }

    @PatchMapping("/user/{userId}/toggle-suspend")
    public ResponseEntity<String> toggleSuspend(@PathVariable Long userId, HttpServletRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getStatus() == UserStatus.SUSPENDED) {
            user.setStatus(UserStatus.VERIFIED);
        } else {
            user.setStatus(UserStatus.SUSPENDED);
        }
        userRepository.save(user);

        User admin = userRepository.findAll().stream().filter(u -> u.getRole() == Role.SUPERADMIN).findFirst().orElse(null);
        if (admin != null) {
            auditLogService.log(admin, "USER_STATUS_TOGGLED", "USER", user.getId(), request);
        }

        return ResponseEntity.ok("User status updated to: " + user.getStatus());
    }
}
