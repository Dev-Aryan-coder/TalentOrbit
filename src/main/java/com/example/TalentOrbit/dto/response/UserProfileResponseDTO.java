package com.example.TalentOrbit.dto.response;

import com.example.TalentOrbit.enums.Role;
import com.example.TalentOrbit.enums.UserStatus;
import java.time.LocalDateTime;
import java.util.List;

public class UserProfileResponseDTO {
    // Base Account Fields
    private Long userId;
    private String email;
    private String fullName;
    private String avatarUrl;
    private Role role;
    private UserStatus status;
    private LocalDateTime createdAt;

    // Student Specific Fields
    private String institutionName;
    private String aisheCode;
    private String branch;
    private Integer gradYear;
    private Double cgpa;
    private String targetRole;
    private Integer employabilityScore;
    private List<String> skills;

    // Industry Specific Fields
    private String companyName;
    private String cinNumber;
    private String sector;
    private String websiteUrl;
    private String description;

    // Academician Specific Fields
    private String department;

    // Institution Specific Fields
    private String state;
    private String contactPerson;

    public UserProfileResponseDTO() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

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

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getCinNumber() { return cinNumber; }
    public void setCinNumber(String cinNumber) { this.cinNumber = cinNumber; }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public String getWebsiteUrl() { return websiteUrl; }
    public void setWebsiteUrl(String websiteUrl) { this.websiteUrl = websiteUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
}