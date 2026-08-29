package com.example.TalentOrbit.dto.response;

import com.example.TalentOrbit.enums.TrainingProgramStatus;
import java.time.LocalDate;

public class TrainingProgramResponseDTO {
    private Long id;
    private String title;
    private String targetSkillName;
    private LocalDate programDate;
    private Integer studentsRegistered;
    private TrainingProgramStatus status;

    public TrainingProgramResponseDTO() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getTargetSkillName() { return targetSkillName; }
    public void setTargetSkillName(String targetSkillName) { this.targetSkillName = targetSkillName; }
    public LocalDate getProgramDate() { return programDate; }
    public void setProgramDate(LocalDate programDate) { this.programDate = programDate; }
    public Integer getStudentsRegistered() { return studentsRegistered; }
    public void setStudentsRegistered(Integer studentsRegistered) { this.studentsRegistered = studentsRegistered; }
    public TrainingProgramStatus getStatus() { return status; }
    public void setStatus(TrainingProgramStatus status) { this.status = status; }
}
