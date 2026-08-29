package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.SupportTicketCreateDTO;
import com.example.TalentOrbit.dto.response.SupportTicketResponseDTO;
import com.example.TalentOrbit.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support")
public class SupportController {
    @Autowired private SupportService supportService;

    @PostMapping("/ticket/create")
    public ResponseEntity<SupportTicketResponseDTO> createTicket(@RequestBody SupportTicketCreateDTO req) {
        return ResponseEntity.ok(supportService.createTicket(req));
    }

    @GetMapping("/tickets/user/{userId}")
    public ResponseEntity<List<SupportTicketResponseDTO>> getTickets(@PathVariable Long userId) {
        return ResponseEntity.ok(supportService.getTickets(userId));
    }
}
