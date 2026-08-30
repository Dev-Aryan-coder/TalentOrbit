package com.example.TalentOrbit.dto.request;

import java.util.List;

public class AiAssessmentRequestDTO {
    private Long userId;
    private Long skillId;
    private Integer selfRatingOutOf10;
    private Integer selfRating;
    private List<AssessmentQuestionAnswerDTO> answers;

    public AiAssessmentRequestDTO() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getSkillId() { return skillId; }
    public void setSkillId(Long skillId) { this.skillId = skillId; }

    public Integer getSelfRatingOutOf10() { 
        return selfRatingOutOf10 != null ? selfRatingOutOf10 : selfRating; 
    }
    public void setSelfRatingOutOf10(Integer selfRatingOutOf10) { 
        this.selfRatingOutOf10 = selfRatingOutOf10;
        this.selfRating = selfRatingOutOf10;
    }

    public Integer getSelfRating() { 
        return selfRating != null ? selfRating : selfRatingOutOf10; 
    }
    public void setSelfRating(Integer selfRating) { 
        this.selfRating = selfRating; 
        this.selfRatingOutOf10 = selfRating;
    }

    public List<AssessmentQuestionAnswerDTO> getAnswers() { return answers; }
    public void setAnswers(List<AssessmentQuestionAnswerDTO> answers) { this.answers = answers; }
}
