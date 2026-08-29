package com.example.TalentOrbit.dto.request;

import java.time.LocalDate;

public class TrainingProgramCreateDTO {
    private Long institutionUserId;
    private String title;
    private Long targetSkillId;
    private LocalDate programDate;

    public TrainingProgramCreateDTO() {}
    public Long getInstitutionUserId() { return institutionUserId; }
    public void setInstitutionUserId(Long institutionUserId) { this.institutionUserId = institutionUserId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Long getTargetSkillId() { return targetSkillId; }
    public void setTargetSkillId(Long targetSkillId) { this.targetSkillId = targetSkillId; }
    public LocalDate getProgramDate() { return programDate; }
    public void setProgramDate(LocalDate programDate) { this.programDate = programDate; }
}
