package com.example.TalentOrbit.dto.request;

import java.time.LocalDateTime;

public class InterviewCreateDTO {
    private Long applicationId;
    private LocalDateTime scheduledAt;
    private String interviewerName;
    private String meetingLink;
    private String notes;

    public InterviewCreateDTO() {}
    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
    public String getInterviewerName() { return interviewerName; }
    public void setInterviewerName(String interviewerName) { this.interviewerName = interviewerName; }
    public String getMeetingLink() { return meetingLink; }
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
