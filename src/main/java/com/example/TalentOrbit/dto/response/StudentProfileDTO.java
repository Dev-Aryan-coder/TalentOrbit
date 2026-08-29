package com.example.TalentOrbit.dto.response;

public class StudentProfileDTO {
    private Long id;
    private String email;
    private String name;
    private String institutionName;
    private String aisheCode;
    private String branch;
    private Integer gradYear;
    private Double cgpa;
    private String targetRole;
    private Integer employabilityScore;

    public StudentProfileDTO() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getInstitutionName() { return institutionName; }
    public void setInstitutionName(String institutionName) { this.institutionName = institutionName; }
    public String getAisheCode() { return aisheCode; }
    public void setAisheCode(String aisheCode) { this.aisheCode = aisheCode; }
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
}
