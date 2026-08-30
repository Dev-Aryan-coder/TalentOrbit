package com.example.TalentOrbit.dto.response;

import java.util.List;

public class CareerSuggestionResponseDTO {
    private String roleName;
    private String description;
    private Integer fitPercent;
    private List<String> matchedSkills;
    private List<String> missingSkills;

    public CareerSuggestionResponseDTO() {}

    public CareerSuggestionResponseDTO(String roleName, String description, Integer fitPercent, List<String> matchedSkills, List<String> missingSkills) {
        this.roleName = roleName;
        this.description = description;
        this.fitPercent = fitPercent;
        this.matchedSkills = matchedSkills;
        this.missingSkills = missingSkills;
    }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getFitPercent() { return fitPercent; }
    public void setFitPercent(Integer fitPercent) { this.fitPercent = fitPercent; }

    public List<String> getMatchedSkills() { return matchedSkills; }
    public void setMatchedSkills(List<String> matchedSkills) { this.matchedSkills = matchedSkills; }

    public List<String> getMissingSkills() { return missingSkills; }
    public void setMissingSkills(List<String> missingSkills) { this.missingSkills = missingSkills; }
}
