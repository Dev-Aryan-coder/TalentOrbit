package com.example.TalentOrbit.dto.request;

import java.util.List;

public class SkillPreviewRequestDTO {
    private List<Long> skillIds;

    public SkillPreviewRequestDTO() {}
    public SkillPreviewRequestDTO(List<Long> skillIds) { this.skillIds = skillIds; }

    public List<Long> getSkillIds() { return skillIds; }
    public void setSkillIds(List<Long> skillIds) { this.skillIds = skillIds; }
}
