package com.example.TalentOrbit.dto.request;

import java.util.List;

public class ProfileConfirmationRequestDTO {
    private List<Long> confirmedSkillIds;
    private String careerInterest;

    public ProfileConfirmationRequestDTO() {}

    public List<Long> getConfirmedSkillIds() { return confirmedSkillIds; }
    public void setConfirmedSkillIds(List<Long> confirmedSkillIds) { this.confirmedSkillIds = confirmedSkillIds; }

    public String getCareerInterest() { return careerInterest; }
    public void setCareerInterest(String careerInterest) { this.careerInterest = careerInterest; }
}
