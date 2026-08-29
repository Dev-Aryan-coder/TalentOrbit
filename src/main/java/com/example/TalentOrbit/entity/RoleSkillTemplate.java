package com.example.TalentOrbit.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "role_skill_templates")
public class RoleSkillTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String roleName;

    @Column(columnDefinition = "TEXT")
    private String description;

    public RoleSkillTemplate() {}

    public RoleSkillTemplate(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
