package com.example.TalentOrbit.dto.response;

import com.example.TalentOrbit.enums.ProficiencyLevel;
import java.time.LocalDate;

public class SkillProfileResponseDTO {
    private Long skillId;
    private String skillName;
    private String category;
    private ProficiencyLevel proficiency;
    private Boolean isVerified;
    private LocalDate lastAssessed;

    public SkillProfileResponseDTO() {}
    public Long getSkillId() { return skillId; }
    public void setSkillId(Long skillId) { this.skillId = skillId; }
    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public ProficiencyLevel getProficiency() { return proficiency; }
    public void setProficiency(ProficiencyLevel proficiency) { this.proficiency = proficiency; }
    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }
    public LocalDate getLastAssessed() { return lastAssessed; }
    public void setLastAssessed(LocalDate lastAssessed) { this.lastAssessed = lastAssessed; }
}
