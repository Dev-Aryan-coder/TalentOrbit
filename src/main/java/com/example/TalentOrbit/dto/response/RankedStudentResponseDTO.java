package com.example.TalentOrbit.dto.response;

import java.util.List;

public class RankedStudentResponseDTO {
    private Long userId;
    private String name;
    private String institutionName;
    private String branch;
    private Double cgpa;
    private Integer matchScore;
    private List<String> topSkills;
    private List<String> matchedSkills;
    private List<String> missingSkills;

    public RankedStudentResponseDTO() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getInstitutionName() { return institutionName; }
    public void setInstitutionName(String institutionName) { this.institutionName = institutionName; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public Double getCgpa() { return cgpa; }
    public void setCgpa(Double cgpa) { this.cgpa = cgpa; }

    public Integer getMatchScore() { return matchScore; }
    public void setMatchScore(Integer matchScore) { this.matchScore = matchScore; }

    public List<String> getTopSkills() { return topSkills; }
    public void setTopSkills(List<String> topSkills) { this.topSkills = topSkills; }

    public List<String> getMatchedSkills() { return matchedSkills; }
    public void setMatchedSkills(List<String> matchedSkills) { this.matchedSkills = matchedSkills; }

    public List<String> getMissingSkills() { return missingSkills; }
    public void setMissingSkills(List<String> missingSkills) { this.missingSkills = missingSkills; }
}
