package com.example.TalentOrbit.dto.request;

public class PostingSkillDTO {
    private Long skillId;
    private String skillName;
    private Integer weight;
    private Boolean isMandatory;

    public PostingSkillDTO() {}

    public PostingSkillDTO(Long skillId, String skillName, Integer weight) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.weight = weight;
    }

    public Long getSkillId() { return skillId; }
    public void setSkillId(Long skillId) { this.skillId = skillId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }

    public Boolean getIsMandatory() { return isMandatory; }
    public void setIsMandatory(Boolean isMandatory) { this.isMandatory = isMandatory; }
}
