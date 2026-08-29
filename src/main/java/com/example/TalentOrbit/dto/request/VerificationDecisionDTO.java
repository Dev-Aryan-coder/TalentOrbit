package com.example.TalentOrbit.dto.request;

import com.example.TalentOrbit.enums.UserStatus;

public class VerificationDecisionDTO {
    private Long userId;
    private UserStatus decision;
    private String reason;

    public VerificationDecisionDTO() {}
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public UserStatus getDecision() { return decision; }
    public void setDecision(UserStatus decision) { this.decision = decision; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
