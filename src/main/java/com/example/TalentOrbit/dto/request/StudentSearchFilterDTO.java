package com.example.TalentOrbit.dto.request;

import java.util.List;

public class StudentSearchFilterDTO {
    private Long postingId;
    private List<String> requiredSkills;
    private String branch;
    private Integer minGradYear;
    private Double minCgpa;

    public StudentSearchFilterDTO() {}
    public Long getPostingId() { return postingId; }
    public void setPostingId(Long postingId) { this.postingId = postingId; }
    public List<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
    public Integer getMinGradYear() { return minGradYear; }
    public void setMinGradYear(Integer minGradYear) { this.minGradYear = minGradYear; }
    public Double getMinCgpa() { return minCgpa; }
    public void setMinCgpa(Double minCgpa) { this.minCgpa = minCgpa; }
}
