package com.example.TalentOrbit.dto.request;

public class AssessmentSubmissionDTO {
    private Long userId;
    private Long skillId;
    private Integer score;
    private Integer maxScore;

    public AssessmentSubmissionDTO() {}
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getSkillId() { return skillId; }
    public void setSkillId(Long skillId) { this.skillId = skillId; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public Integer getMaxScore() { return maxScore; }
    public void setMaxScore(Integer maxScore) { this.maxScore = maxScore; }
}
