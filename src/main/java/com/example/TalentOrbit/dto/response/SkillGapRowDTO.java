package com.example.TalentOrbit.dto.response;

public class SkillGapRowDTO {
    private String skillName;
    private Integer demandPercentage;
    private Integer supplyPercentage;
    private Integer netDeficitPercentage;
    private Integer deficitPercentage;
    private Integer affectedStudents;

    public SkillGapRowDTO() {}

    public SkillGapRowDTO(String skillName, Integer demandPercentage, Integer supplyPercentage, Integer netDeficitPercentage, Integer affectedStudents) {
        this.skillName = skillName;
        this.demandPercentage = demandPercentage;
        this.supplyPercentage = supplyPercentage;
        this.netDeficitPercentage = netDeficitPercentage;
        this.deficitPercentage = netDeficitPercentage;
        this.affectedStudents = affectedStudents;
    }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public Integer getDemandPercentage() { return demandPercentage; }
    public void setDemandPercentage(Integer demandPercentage) { this.demandPercentage = demandPercentage; }

    public Integer getSupplyPercentage() { return supplyPercentage; }
    public void setSupplyPercentage(Integer supplyPercentage) { this.supplyPercentage = supplyPercentage; }

    public Integer getNetDeficitPercentage() { return netDeficitPercentage; }
    public void setNetDeficitPercentage(Integer netDeficitPercentage) { 
        this.netDeficitPercentage = netDeficitPercentage; 
        this.deficitPercentage = netDeficitPercentage;
    }

    public Integer getDeficitPercentage() { return deficitPercentage != null ? deficitPercentage : netDeficitPercentage; }
    public void setDeficitPercentage(Integer deficitPercentage) { 
        this.deficitPercentage = deficitPercentage;
        this.netDeficitPercentage = deficitPercentage;
    }

    public Integer getAffectedStudents() { return affectedStudents; }
    public void setAffectedStudents(Integer affectedStudents) { this.affectedStudents = affectedStudents; }
}
