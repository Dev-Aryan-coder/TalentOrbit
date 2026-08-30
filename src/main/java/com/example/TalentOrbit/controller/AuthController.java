package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.LoginRequestDTO;
import com.example.TalentOrbit.dto.request.SignupCredentialsDTO;
import com.example.TalentOrbit.dto.response.UserResponseDTO;
import com.example.TalentOrbit.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> signup(@Valid @RequestBody SignupCredentialsDTO req) {
        return ResponseEntity.ok(authService.signup(req));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody LoginRequestDTO req) {
        return ResponseEntity.ok(authService.login(req));
    }
}
