package com.example.TalentOrbit.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "student_details")
public class StudentDetails {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(name = "institution_name")
    private String institutionName;

    @Column(name = "aishe_code")
    private String aisheCode;

    private String branch;

    @Column(name = "grad_year")
    private Integer gradYear;

    private Double cgpa;

    @Column(name = "target_role")
    private String targetRole;

    @Column(name = "employability_score")
    private Integer employabilityScore = 0;

    public StudentDetails() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

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
