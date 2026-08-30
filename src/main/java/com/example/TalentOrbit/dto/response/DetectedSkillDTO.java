package com.example.TalentOrbit.dto.response;

public class DetectedSkillDTO {
    private Long skillId;
    private String skillName;

    public DetectedSkillDTO() {}
    public DetectedSkillDTO(Long skillId, String skillName) {
        this.skillId = skillId;
        this.skillName = skillName;
    }

    public Long getSkillId() { return skillId; }
    public void setSkillId(Long skillId) { this.skillId = skillId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
}
