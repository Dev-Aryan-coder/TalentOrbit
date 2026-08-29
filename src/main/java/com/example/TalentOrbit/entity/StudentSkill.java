package com.example.TalentOrbit.entity;

import com.example.TalentOrbit.enums.ProficiencyLevel;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "student_skills", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "skill_id"})
})
public class StudentSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProficiencyLevel proficiency = ProficiencyLevel.BEGINNER;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;

    @Column(name = "last_assessed")
    private LocalDate lastAssessed;

    public StudentSkill() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Skill getSkill() { return skill; }
    public void setSkill(Skill skill) { this.skill = skill; }

    public ProficiencyLevel getProficiency() { return proficiency; }
    public void setProficiency(ProficiencyLevel proficiency) { this.proficiency = proficiency; }

    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }

    public LocalDate getLastAssessed() { return lastAssessed; }
    public void setLastAssessed(LocalDate lastAssessed) { this.lastAssessed = lastAssessed; }
}
