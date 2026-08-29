package com.example.TalentOrbit.dto.request;

import com.example.TalentOrbit.enums.FlagStatus;

public class FlagDecisionDTO {
    private FlagStatus status;

    public FlagDecisionDTO() {}
    public FlagStatus getStatus() { return status; }
    public void setStatus(FlagStatus status) { this.status = status; }
}
