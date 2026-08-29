package com.example.TalentOrbit.dto.request;

import com.example.TalentOrbit.enums.InterviewStatus;

public class InterviewStatusUpdateDTO {
    private InterviewStatus status;
    private String notes;

    public InterviewStatusUpdateDTO() {}
    public InterviewStatus getStatus() { return status; }
    public void setStatus(InterviewStatus status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
