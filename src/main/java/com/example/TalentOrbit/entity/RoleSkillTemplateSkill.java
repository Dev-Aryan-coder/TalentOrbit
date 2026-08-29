package com.example.TalentOrbit.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "role_skill_template_skills")
public class RoleSkillTemplateSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private RoleSkillTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(nullable = false)
    private Integer weight = 3;

    public RoleSkillTemplateSkill() {}

    public RoleSkillTemplateSkill(RoleSkillTemplate template, Skill skill, Integer weight) {
        this.template = template;
        this.skill = skill;
        this.weight = weight;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RoleSkillTemplate getTemplate() { return template; }
    public void setTemplate(RoleSkillTemplate template) { this.template = template; }

    public Skill getSkill() { return skill; }
    public void setSkill(Skill skill) { this.skill = skill; }

    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }
}
