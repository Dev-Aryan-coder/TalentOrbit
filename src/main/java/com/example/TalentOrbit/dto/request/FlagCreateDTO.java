package com.example.TalentOrbit.dto.request;

import com.example.TalentOrbit.enums.FlagItemType;

public class FlagCreateDTO {
    private Long reportedByUserId;
    private FlagItemType itemType;
    private Long itemId;
    private String reason;

    public FlagCreateDTO() {}
    public Long getReportedByUserId() { return reportedByUserId; }
    public void setReportedByUserId(Long reportedByUserId) { this.reportedByUserId = reportedByUserId; }
    public FlagItemType getItemType() { return itemType; }
    public void setItemType(FlagItemType itemType) { this.itemType = itemType; }
    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
