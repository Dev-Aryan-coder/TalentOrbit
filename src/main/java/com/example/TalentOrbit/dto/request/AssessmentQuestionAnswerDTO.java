package com.example.TalentOrbit.dto.request;

public class AssessmentQuestionAnswerDTO {
    private Long questionId;
    private String selectedOption; // "A", "B", "C", "D"

    public AssessmentQuestionAnswerDTO() {}

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }

    public String getSelectedOption() { return selectedOption; }
    public void setSelectedOption(String selectedOption) { this.selectedOption = selectedOption; }
}
