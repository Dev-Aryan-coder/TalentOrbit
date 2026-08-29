package com.example.TalentOrbit.controller;

import com.example.TalentOrbit.dto.request.ChatMessageRequestDTO;
import com.example.TalentOrbit.dto.response.ChatMessageHistoryDTO;
import com.example.TalentOrbit.dto.response.ChatMessageResponseDTO;
import com.example.TalentOrbit.dto.response.ChatSessionResponseDTO;
import com.example.TalentOrbit.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @Autowired private ChatbotService chatbotService;

    @PostMapping("/message")
    public ResponseEntity<ChatMessageResponseDTO> sendMessage(@RequestBody ChatMessageRequestDTO req) {
        return ResponseEntity.ok(chatbotService.sendMessage(req));
    }

    @GetMapping("/sessions/{userId}")
    public ResponseEntity<List<ChatSessionResponseDTO>> getUserSessions(@PathVariable Long userId) {
        return ResponseEntity.ok(chatbotService.getUserSessions(userId));
    }

    @GetMapping("/sessions/{sessionId}/messages")
    public ResponseEntity<List<ChatMessageHistoryDTO>> getSessionMessages(@PathVariable Long sessionId) {
        return ResponseEntity.ok(chatbotService.getSessionMessages(sessionId));
    }
}
