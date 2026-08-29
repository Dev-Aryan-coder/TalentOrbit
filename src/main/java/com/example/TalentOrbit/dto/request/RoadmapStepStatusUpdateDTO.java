package com.example.TalentOrbit.dto.request;

import com.example.TalentOrbit.enums.RoadmapStepStatus;

public class RoadmapStepStatusUpdateDTO {
    private RoadmapStepStatus status;

    public RoadmapStepStatusUpdateDTO() {}
    public RoadmapStepStatus getStatus() { return status; }
    public void setStatus(RoadmapStepStatus status) { this.status = status; }
}
