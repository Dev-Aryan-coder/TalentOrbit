package com.example.TalentOrbit.dto.request;

import java.util.List;

public class InterestTagUpdateDTO {
    private Long userId;
    private List<Long> skillIds;

    public InterestTagUpdateDTO() {}
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public List<Long> getSkillIds() { return skillIds; }
    public void setSkillIds(List<Long> skillIds) { this.skillIds = skillIds; }
}
