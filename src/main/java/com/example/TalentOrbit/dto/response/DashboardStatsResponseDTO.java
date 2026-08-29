package com.example.TalentOrbit.dto.response;

import java.util.Map;

public class DashboardStatsResponseDTO {
    private Map<String, Object> stats;

    public DashboardStatsResponseDTO() {}
    public DashboardStatsResponseDTO(Map<String, Object> stats) { this.stats = stats; }
    public Map<String, Object> getStats() { return stats; }
    public void setStats(Map<String, Object> stats) { this.stats = stats; }
}
