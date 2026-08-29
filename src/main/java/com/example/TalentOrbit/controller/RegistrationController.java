package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.RoleDetailsRequestDTO;
import com.example.TalentOrbit.dto.response.RegistrationStatusResponseDTO;
import com.example.TalentOrbit.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {
    @Autowired private RegistrationService registrationService;

    @PostMapping("/complete")
    public ResponseEntity<RegistrationStatusResponseDTO> complete(@RequestBody RoleDetailsRequestDTO req) {
        return ResponseEntity.ok(registrationService.completeRegistration(req));
    }
}
