package com.example.TalentOrbit.service;

import com.example.TalentOrbit.dto.request.SupportTicketCreateDTO;
import com.example.TalentOrbit.dto.response.SupportTicketResponseDTO;
import com.example.TalentOrbit.entity.SupportTicket;
import com.example.TalentOrbit.entity.User;
import com.example.TalentOrbit.enums.SupportTicketStatus;
import com.example.TalentOrbit.exception.ResourceNotFoundException;
import com.example.TalentOrbit.repository.SupportTicketRepository;
import com.example.TalentOrbit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupportService {
    @Autowired private SupportTicketRepository ticketRepository;
    @Autowired private UserRepository userRepository;

    public SupportTicketResponseDTO createTicket(SupportTicketCreateDTO req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        SupportTicket ticket = new SupportTicket();
        ticket.setUser(user);
        ticket.setSubject(req.getSubject());
        ticket.setMessage(req.getMessage());
        ticket.setStatus(SupportTicketStatus.OPEN);
        SupportTicket saved = ticketRepository.save(ticket);

        SupportTicketResponseDTO dto = new SupportTicketResponseDTO();
        dto.setId(saved.getId());
        dto.setSubject(saved.getSubject());
        dto.setMessage(saved.getMessage());
        dto.setStatus(saved.getStatus());
        dto.setCreatedAt(saved.getCreatedAt());
        return dto;
    }

    public List<SupportTicketResponseDTO> getTickets(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return ticketRepository.findByUser(user).stream().map(t -> {
            SupportTicketResponseDTO dto = new SupportTicketResponseDTO();
            dto.setId(t.getId());
            dto.setSubject(t.getSubject());
            dto.setMessage(t.getMessage());
            dto.setStatus(t.getStatus());
            dto.setCreatedAt(t.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }
}
