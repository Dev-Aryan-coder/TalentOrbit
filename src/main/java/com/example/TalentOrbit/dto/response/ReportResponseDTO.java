package com.example.TalentOrbit.dto.response;

import com.example.TalentOrbit.enums.ReportType;
import java.time.LocalDateTime;

public class ReportResponseDTO {
    private Long id;
    private ReportType reportType;
    private String dateRange;
    private String filePath;
    private LocalDateTime generatedAt;

    public ReportResponseDTO() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ReportType getReportType() { return reportType; }
    public void setReportType(ReportType reportType) { this.reportType = reportType; }
    public String getDateRange() { return dateRange; }
    public void setDateRange(String dateRange) { this.dateRange = dateRange; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
}
