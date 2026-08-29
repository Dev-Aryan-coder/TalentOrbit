package com.example.TalentOrbit.dto.response;

import com.example.TalentOrbit.enums.RoadmapStepStatus;

public class RoadmapStepResponseDTO {
    private Long id;
    private Integer stepOrder;
    private String title;
    private String description;
    private RoadmapStepStatus status;
    private String linkedSkillName;

    public RoadmapStepResponseDTO() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getStepOrder() { return stepOrder; }
    public void setStepOrder(Integer stepOrder) { this.stepOrder = stepOrder; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public RoadmapStepStatus getStatus() { return status; }
    public void setStatus(RoadmapStepStatus status) { this.status = status; }
    public String getLinkedSkillName() { return linkedSkillName; }
    public void setLinkedSkillName(String linkedSkillName) { this.linkedSkillName = linkedSkillName; }
}
