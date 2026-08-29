package com.example.TalentOrbit.entity;

import com.example.TalentOrbit.enums.TrainingProgramStatus;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "training_programs")
public class TrainingProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_user_id", nullable = false)
    private User institutionUser;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_skill_id")
    private Skill targetSkill;

    @Column(name = "program_date")
    private LocalDate programDate;

    @Column(name = "students_registered")
    private Integer studentsRegistered = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrainingProgramStatus status = TrainingProgramStatus.PLANNED;

    public TrainingProgram() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getInstitutionUser() { return institutionUser; }
    public void setInstitutionUser(User institutionUser) { this.institutionUser = institutionUser; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Skill getTargetSkill() { return targetSkill; }
    public void setTargetSkill(Skill targetSkill) { this.targetSkill = targetSkill; }

    public LocalDate getProgramDate() { return programDate; }
    public void setProgramDate(LocalDate programDate) { this.programDate = programDate; }

    public Integer getStudentsRegistered() { return studentsRegistered; }
    public void setStudentsRegistered(Integer studentsRegistered) { this.studentsRegistered = studentsRegistered; }

    public TrainingProgramStatus getStatus() { return status; }
    public void setStatus(TrainingProgramStatus status) { this.status = status; }
}
