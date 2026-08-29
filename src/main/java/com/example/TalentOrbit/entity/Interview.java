package com.example.TalentOrbit.entity;

import com.example.TalentOrbit.enums.InterviewStatus;
import com.example.TalentOrbit.enums.OverallRecommendation;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "interviews")
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;

    @Column(name = "interviewer_name")
    private String interviewerName;

    @Column(name = "meeting_link")
    private String meetingLink;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewStatus status = InterviewStatus.SCHEDULED;

    @Column(name = "technical_score")
    private Integer technicalScore; // 1-5

    @Column(name = "communication_score")
    private Integer communicationScore; // 1-5

    @Column(name = "culture_fit_score")
    private Integer cultureFitScore; // 1-5

    @Enumerated(EnumType.STRING)
    @Column(name = "overall_recommendation")
    private OverallRecommendation overallRecommendation;

    @Column(columnDefinition = "TEXT")
    private String notes;

    public Interview() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Application getApplication() { return application; }
    public void setApplication(Application application) { this.application = application; }

    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }

    public String getInterviewerName() { return interviewerName; }
    public void setInterviewerName(String interviewerName) { this.interviewerName = interviewerName; }

    public String getMeetingLink() { return meetingLink; }
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }

    public InterviewStatus getStatus() { return status; }
    public void setStatus(InterviewStatus status) { this.status = status; }

    public Integer getTechnicalScore() { return technicalScore; }
    public void setTechnicalScore(Integer technicalScore) { this.technicalScore = technicalScore; }

    public Integer getCommunicationScore() { return communicationScore; }
    public void setCommunicationScore(Integer communicationScore) { this.communicationScore = communicationScore; }

    public Integer getCultureFitScore() { return cultureFitScore; }
    public void setCultureFitScore(Integer cultureFitScore) { this.cultureFitScore = cultureFitScore; }

    public OverallRecommendation getOverallRecommendation() { return overallRecommendation; }
    public void setOverallRecommendation(OverallRecommendation overallRecommendation) { this.overallRecommendation = overallRecommendation; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
