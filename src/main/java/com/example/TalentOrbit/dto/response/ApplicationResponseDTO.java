package com.example.TalentOrbit.dto.response;

import com.example.TalentOrbit.enums.ApplicationStatus;
import java.time.LocalDateTime;
import java.util.List;

public class ApplicationResponseDTO {
    private Long id;
    private Long postingId;
    private String postingTitle;
    private String companyName;
    private Long userId;
    private String studentName;
    private LocalDateTime appliedAt;
    private ApplicationStatus status;
    private Integer matchScore;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private Double mentorRating;
    private String mentorFeedback;

    public ApplicationResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPostingId() { return postingId; }
    public void setPostingId(Long postingId) { this.postingId = postingId; }

    public String getPostingTitle() { return postingTitle; }
    public void setPostingTitle(String postingTitle) { this.postingTitle = postingTitle; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public Integer getMatchScore() { return matchScore; }
    public void setMatchScore(Integer matchScore) { this.matchScore = matchScore; }

    public List<String> getMatchedSkills() { return matchedSkills; }
    public void setMatchedSkills(List<String> matchedSkills) { this.matchedSkills = matchedSkills; }

    public List<String> getMissingSkills() { return missingSkills; }
    public void setMissingSkills(List<String> missingSkills) { this.missingSkills = missingSkills; }

    public Double getMentorRating() { return mentorRating; }
    public void setMentorRating(Double mentorRating) { this.mentorRating = mentorRating; }

    public String getMentorFeedback() { return mentorFeedback; }
    public void setMentorFeedback(String mentorFeedback) { this.mentorFeedback = mentorFeedback; }
}
