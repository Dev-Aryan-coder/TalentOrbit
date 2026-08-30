package com.example.TalentOrbit.dto.response;

import com.example.TalentOrbit.enums.Role;
import java.util.Map;

public class DashboardStatsResponseDTO {
    private Role role;
    private Long totalActivePostings;
    private Long totalApplications;
    private Long totalSkillsInMaster;
    private Map<String, Object> stats;

    public DashboardStatsResponseDTO() {}

    public DashboardStatsResponseDTO(Map<String, Object> stats) {
        this.stats = stats;
    }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Long getTotalActivePostings() { return totalActivePostings; }
    public void setTotalActivePostings(Long totalActivePostings) { this.totalActivePostings = totalActivePostings; }

    public Long getTotalApplications() { return totalApplications; }
    public void setTotalApplications(Long totalApplications) { this.totalApplications = totalApplications; }

    public Long getTotalSkillsInMaster() { return totalSkillsInMaster; }
    public void setTotalSkillsInMaster(Long totalSkillsInMaster) { this.totalSkillsInMaster = totalSkillsInMaster; }

    public Map<String, Object> getStats() { return stats; }
    public void setStats(Map<String, Object> stats) { this.stats = stats; }
}
