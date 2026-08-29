package com.example.TalentOrbit.dto.response;

import java.time.LocalDateTime;

public class ChatMessageResponseDTO {
    private Long sessionId;
    private String reply;
    private LocalDateTime timestamp;

    public ChatMessageResponseDTO() {}

    public ChatMessageResponseDTO(Long sessionId, String reply, LocalDateTime timestamp) {
        this.sessionId = sessionId;
        this.reply = reply;
        this.timestamp = timestamp;
    }

    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }

    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
