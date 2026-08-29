package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.VerificationDecisionDTO;
import com.example.TalentOrbit.dto.response.PendingRegistrationResponseDTO;
import com.example.TalentOrbit.dto.response.UserSummaryResponseDTO;
import com.example.TalentOrbit.email.EmailService;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.enums.Role;
import com.example.TalentOrbit.enums.UserStatus;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuperAdminService {
    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;

    public List<PendingRegistrationResponseDTO> getPendingVerifications() {
        return userRepository.findByStatus(UserStatus.PENDING_VERIFICATION).stream().map(u -> {
            PendingRegistrationResponseDTO dto = new PendingRegistrationResponseDTO();
            dto.setUserId(u.getId());
            dto.setEmail(u.getEmail());
            dto.setRole(u.getRole());
            dto.setStatus(u.getStatus());
            dto.setCreatedAt(u.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    public void decideVerification(VerificationDecisionDTO req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setStatus(req.getDecision());
        userRepository.save(user);
        emailService.sendRegistrationDecisionEmail(user.getEmail(), user.getRole().name(), user.getStatus().name(), req.getReason());
    }

    public List<UserSummaryResponseDTO> getAllUsers(Role role) {
        List<User> list = (role != null) ? userRepository.findByRole(role) : userRepository.findAll();
        return list.stream().map(u -> {
            UserSummaryResponseDTO dto = new UserSummaryResponseDTO();
            dto.setId(u.getId());
            dto.setEmail(u.getEmail());
            dto.setRole(u.getRole());
            dto.setStatus(u.getStatus());
            return dto;
        }).collect(Collectors.toList());
    }
}
