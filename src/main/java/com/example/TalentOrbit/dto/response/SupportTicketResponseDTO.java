package com.example.TalentOrbit.dto.response;

import com.example.TalentOrbit.enums.SupportTicketStatus;
import java.time.LocalDateTime;

public class SupportTicketResponseDTO {
    private Long id;
    private String subject;
    private String message;
    private SupportTicketStatus status;
    private LocalDateTime createdAt;

    public SupportTicketResponseDTO() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public SupportTicketStatus getStatus() { return status; }
    public void setStatus(SupportTicketStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
