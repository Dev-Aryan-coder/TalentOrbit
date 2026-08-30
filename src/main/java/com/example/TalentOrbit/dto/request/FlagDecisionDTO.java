package com.example.TalentOrbit.dto.request;

import com.example.TalentOrbit.enums.FlagStatus;

public class FlagDecisionDTO {
    private Long flagId;
    private FlagStatus status;
    private FlagStatus decision;

    public FlagDecisionDTO() {}

    public Long getFlagId() { return flagId; }
    public void setFlagId(Long flagId) { this.flagId = flagId; }

    public FlagStatus getStatus() { return status != null ? status : decision; }
    public void setStatus(FlagStatus status) { 
        this.status = status; 
        this.decision = status; 
    }

    public FlagStatus getDecision() { return decision != null ? decision : status; }
    public void setDecision(FlagStatus decision) { 
        this.decision = decision; 
        this.status = decision; 
    }
}
