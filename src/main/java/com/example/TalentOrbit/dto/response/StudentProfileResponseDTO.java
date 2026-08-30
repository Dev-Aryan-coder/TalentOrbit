package com.example.TalentOrbit.dto.response;

import java.util.List;

public class StudentProfileResponseDTO {
    private Long id;
    private Long userId;
    private String name;
    private String institutionName;
    private String branch;
    private Integer gradYear;
    private Double cgpa;
    private String targetRole;
    private Integer employabilityScore;
    private List<String> skills;

    public StudentProfileResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getInstitutionName() { return institutionName; }
    public void setInstitutionName(String institutionName) { this.institutionName = institutionName; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public Integer getGradYear() { return gradYear; }
    public void setGradYear(Integer gradYear) { this.gradYear = gradYear; }

    public Double getCgpa() { return cgpa; }
    public void setCgpa(Double cgpa) { this.cgpa = cgpa; }

    public String getTargetRole() { return targetRole; }
    public void setTargetRole(String targetRole) { this.targetRole = targetRole; }

    public Integer getEmployabilityScore() { return employabilityScore; }
    public void setEmployabilityScore(Integer employabilityScore) { this.employabilityScore = employabilityScore; }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
}
