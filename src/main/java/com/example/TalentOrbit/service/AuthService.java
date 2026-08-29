package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.LoginRequestDTO;
import com.example.TalentOrbit.dto.request.SignupCredentialsDTO;
import com.example.TalentOrbit.dto.response.UserResponseDTO;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.enums.UserStatus;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO signup(SignupCredentialsDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with email already exists");
        }
        User user = new User(request.getEmail(), request.getPassword(), request.getRole(), UserStatus.PENDING_VERIFICATION);
        User saved = userRepository.save(user);
        return new UserResponseDTO(saved.getId(), saved.getEmail(), saved.getRole(), saved.getStatus(), saved.getCreatedAt());
    }

    public UserResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!user.getPasswordHash().equals(request.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        if (user.getStatus() != UserStatus.VERIFIED) {
            throw new IllegalStateException("Account is currently " + user.getStatus() + ". Approval by SuperAdmin is required before logging in.");
        }
        return new UserResponseDTO(user.getId(), user.getEmail(), user.getRole(), user.getStatus(), user.getCreatedAt());
    }
}
