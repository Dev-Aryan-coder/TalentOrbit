package com.example.TalentOrbit.dto.response;

import java.util.Map;

public class AiAssessmentResponseDTO {
    private String skillName;
    private Integer selfRating;
    private Integer selfRatingPercentage;
    private Integer actualScorePercentage;
    private Integer confidenceGapPercentage;
    private String gapCategory;
    private Map<String, Integer> topicBreakdown;
    private String aiExplanationAndActionPlan;
    private String updatedProficiency;
    private Boolean isVerified;
    private Integer newEmployabilityScore;

    public AiAssessmentResponseDTO() {}

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public Integer getSelfRating() { return selfRating; }
    public void setSelfRating(Integer selfRating) { this.selfRating = selfRating; }

    public Integer getSelfRatingPercentage() { return selfRatingPercentage; }
    public void setSelfRatingPercentage(Integer selfRatingPercentage) { this.selfRatingPercentage = selfRatingPercentage; }

    public Integer getActualScorePercentage() { return actualScorePercentage; }
    public void setActualScorePercentage(Integer actualScorePercentage) { 
        this.actualScorePercentage = actualScorePercentage; 
    }
    public Integer getAssessmentScorePercentage() { return actualScorePercentage; }
    public void setAssessmentScorePercentage(Integer val) { this.actualScorePercentage = val; }

    public Integer getConfidenceGapPercentage() { return confidenceGapPercentage; }
    public void setConfidenceGapPercentage(Integer confidenceGapPercentage) { 
        this.confidenceGapPercentage = confidenceGapPercentage; 
    }
    public Integer getConfidenceGap() { return confidenceGapPercentage; }
    public void setConfidenceGap(Integer val) { this.confidenceGapPercentage = val; }

    public String getGapCategory() { return gapCategory; }
    public void setGapCategory(String gapCategory) { this.gapCategory = gapCategory; }

    public Map<String, Integer> getTopicBreakdown() { return topicBreakdown; }
    public void setTopicBreakdown(Map<String, Integer> topicBreakdown) { this.topicBreakdown = topicBreakdown; }

    public String getAiExplanationAndActionPlan() { return aiExplanationAndActionPlan; }
    public void setAiExplanationAndActionPlan(String aiExplanationAndActionPlan) { 
        this.aiExplanationAndActionPlan = aiExplanationAndActionPlan; 
    }
    public String getAiAnalysisExplanation() { return aiExplanationAndActionPlan; }
    public void setAiAnalysisExplanation(String val) { this.aiExplanationAndActionPlan = val; }

    public String getUpdatedProficiency() { return updatedProficiency; }
    public void setUpdatedProficiency(String updatedProficiency) { this.updatedProficiency = updatedProficiency; }

    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }

    public Integer getNewEmployabilityScore() { return newEmployabilityScore; }
    public void setNewEmployabilityScore(Integer newEmployabilityScore) { 
        this.newEmployabilityScore = newEmployabilityScore; 
    }
    public Integer getUpdatedEmployabilityScore() { return newEmployabilityScore; }
    public void setUpdatedEmployabilityScore(Integer val) { this.newEmployabilityScore = val; }
}
