package com.example.TalentOrbit.dto.response;

import com.example.TalentOrbit.enums.InterviewStatus;
import java.time.LocalDateTime;

public class InterviewResponseDTO {
    private Long id;
    private Long applicationId;
    private String studentName;
    private String postingTitle;
    private LocalDateTime scheduledAt;
    private String interviewerName;
    private String meetingLink;
    private InterviewStatus status;
    private String notes;

    public InterviewResponseDTO() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getPostingTitle() { return postingTitle; }
    public void setPostingTitle(String postingTitle) { this.postingTitle = postingTitle; }
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
    public String getInterviewerName() { return interviewerName; }
    public void setInterviewerName(String interviewerName) { this.interviewerName = interviewerName; }
    public String getMeetingLink() { return meetingLink; }
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }
    public InterviewStatus getStatus() { return status; }
    public void setStatus(InterviewStatus status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
