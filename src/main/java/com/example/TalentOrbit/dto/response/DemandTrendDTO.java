package com.example.TalentOrbit.dto.response;

public class DemandTrendDTO {
    private String month;
    private String skillName;
    private long demandCount;

    public DemandTrendDTO() {}

    public DemandTrendDTO(String month, String skillName, long demandCount) {
        this.month = month;
        this.skillName = skillName;
        this.demandCount = demandCount;
    }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public long getDemandCount() { return demandCount; }
    public void setDemandCount(long demandCount) { this.demandCount = demandCount; }
}
