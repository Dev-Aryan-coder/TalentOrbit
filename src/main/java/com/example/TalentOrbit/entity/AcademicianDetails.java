package com.example.TalentOrbit.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "academician_details")
public class AcademicianDetails {
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

    private String department;
    private String designation;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(columnDefinition = "TEXT")
    private String bio;

    public AcademicianDetails() {}

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

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
}
