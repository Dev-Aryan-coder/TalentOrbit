package com.example.TalentOrbit.dto.request;

public class SupportTicketCreateDTO {
    private Long userId;
    private String subject;
    private String message;

    public SupportTicketCreateDTO() {}
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
