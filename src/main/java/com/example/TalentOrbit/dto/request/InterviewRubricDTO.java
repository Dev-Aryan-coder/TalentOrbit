package com.example.TalentOrbit.dto.request;

import com.example.TalentOrbit.enums.OverallRecommendation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class InterviewRubricDTO {
    @Min(1) @Max(5)
    private Integer technicalScore;

    @Min(1) @Max(5)
    private Integer communicationScore;

    @Min(1) @Max(5)
    private Integer cultureFitScore;

    private OverallRecommendation overallRecommendation;
    private String notes;

    public InterviewRubricDTO() {}

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
