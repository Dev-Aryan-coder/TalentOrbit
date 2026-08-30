package com.example.TalentOrbit.dto.request;

import com.example.TalentOrbit.enums.ApplicationStatus;
import java.math.BigDecimal;

public class ApplicationStatusUpdateDTO {
    private ApplicationStatus status;
    private Double mentorRating;
    private String mentorFeedback;
    private BigDecimal offeredPackage;

    public ApplicationStatusUpdateDTO() {}

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public Double getMentorRating() { return mentorRating; }
    public void setMentorRating(Double mentorRating) { this.mentorRating = mentorRating; }

    public String getMentorFeedback() { return mentorFeedback; }
    public void setMentorFeedback(String mentorFeedback) { this.mentorFeedback = mentorFeedback; }

    public BigDecimal getOfferedPackage() { return offeredPackage; }
    public void setOfferedPackage(BigDecimal offeredPackage) { this.offeredPackage = offeredPackage; }
}
