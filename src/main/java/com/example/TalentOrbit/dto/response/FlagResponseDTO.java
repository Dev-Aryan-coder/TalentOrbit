package com.example.TalentOrbit.dto.response;

import com.example.TalentOrbit.enums.FlagItemType;
import com.example.TalentOrbit.enums.FlagStatus;
import java.time.LocalDateTime;

public class FlagResponseDTO {
    private Long id;
    private FlagItemType itemType;
    private Long itemId;
    private String reportedByEmail;
    private String reason;
    private FlagStatus status;
    private LocalDateTime createdAt;

    public FlagResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public FlagItemType getItemType() { return itemType; }
    public void setItemType(FlagItemType itemType) { this.itemType = itemType; }

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }

    public String getReportedByEmail() { return reportedByEmail; }
    public void setReportedByEmail(String reportedByEmail) { this.reportedByEmail = reportedByEmail; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public FlagStatus getStatus() { return status; }
    public void setStatus(FlagStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
