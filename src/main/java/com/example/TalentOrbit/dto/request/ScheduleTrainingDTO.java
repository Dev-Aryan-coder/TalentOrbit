package com.example.TalentOrbit.dto.request;

import java.time.LocalDate;

public class ScheduleTrainingDTO {
    private Long institutionId;
    private LocalDate scheduledDate;

    public ScheduleTrainingDTO() {}

    public Long getInstitutionId() { return institutionId; }
    public void setInstitutionId(Long institutionId) { this.institutionId = institutionId; }

    public LocalDate getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }
}
