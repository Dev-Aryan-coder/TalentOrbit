package com.example.TalentOrbit.dto.response;

public class SkillDemandTrendDTO {
    private String skillName;
    private Integer demandSharePercentage;
    private String trendDirection;
    private Integer activePostingsCount;

    public SkillDemandTrendDTO() {}
    public SkillDemandTrendDTO(String skillName, Integer demandSharePercentage, String trendDirection, Integer activePostingsCount) {
        this.skillName = skillName;
        this.demandSharePercentage = demandSharePercentage;
        this.trendDirection = trendDirection;
        this.activePostingsCount = activePostingsCount;
    }
    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }
    public Integer getDemandSharePercentage() { return demandSharePercentage; }
    public void setDemandSharePercentage(Integer demandSharePercentage) { this.demandSharePercentage = demandSharePercentage; }
    public String getTrendDirection() { return trendDirection; }
    public void setTrendDirection(String trendDirection) { this.trendDirection = trendDirection; }
    public Integer getActivePostingsCount() { return activePostingsCount; }
    public void setActivePostingsCount(Integer activePostingsCount) { this.activePostingsCount = activePostingsCount; }
}
