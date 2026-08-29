package com.example.TalentOrbit.dto.response;

import com.example.TalentOrbit.enums.ProficiencyLevel;
import java.util.Map;

public class AiAssessmentResponseDTO {
    private String skillName;
    private Integer selfRatingPercentage; // e.g. 80%
    private Integer assessmentScorePercentage; // e.g. 62%
    private Integer confidenceGap; // self - assessment, e.g. +18%
    private String gapCategory; // "GOOD_ALIGNMENT", "MODERATE_GAP", "SIGNIFICANT_GAP"
    private Map<String, Integer> topicBreakdown; // e.g. {"JOINs": 40, "GROUP BY": 80, ...}
    private ProficiencyLevel calculatedProficiency;
    private Integer updatedEmployabilityScore;
    private String aiAnalysisExplanation;
    private String aiActionPlan;

    public AiAssessmentResponseDTO() {}

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public Integer getSelfRatingPercentage() { return selfRatingPercentage; }
    public void setSelfRatingPercentage(Integer selfRatingPercentage) { this.selfRatingPercentage = selfRatingPercentage; }

    public Integer getAssessmentScorePercentage() { return assessmentScorePercentage; }
    public void setAssessmentScorePercentage(Integer assessmentScorePercentage) { this.assessmentScorePercentage = assessmentScorePercentage; }

    public Integer getConfidenceGap() { return confidenceGap; }
    public void setConfidenceGap(Integer confidenceGap) { this.confidenceGap = confidenceGap; }

    public String getGapCategory() { return gapCategory; }
    public void setGapCategory(String gapCategory) { this.gapCategory = gapCategory; }

    public Map<String, Integer> getTopicBreakdown() { return topicBreakdown; }
    public void setTopicBreakdown(Map<String, Integer> topicBreakdown) { this.topicBreakdown = topicBreakdown; }

    public ProficiencyLevel getCalculatedProficiency() { return calculatedProficiency; }
    public void setCalculatedProficiency(ProficiencyLevel calculatedProficiency) { this.calculatedProficiency = calculatedProficiency; }

    public Integer getUpdatedEmployabilityScore() { return updatedEmployabilityScore; }
    public void setUpdatedEmployabilityScore(Integer updatedEmployabilityScore) { this.updatedEmployabilityScore = updatedEmployabilityScore; }

    public String getAiAnalysisExplanation() { return aiAnalysisExplanation; }
    public void setAiAnalysisExplanation(String aiAnalysisExplanation) { this.aiAnalysisExplanation = aiAnalysisExplanation; }

    public String getAiActionPlan() { return aiActionPlan; }
    public void setAiActionPlan(String aiActionPlan) { this.aiActionPlan = aiActionPlan; }
}
