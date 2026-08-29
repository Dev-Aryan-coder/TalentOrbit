package com.example.TalentOrbit.dto.request;

import com.example.TalentOrbit.enums.ReportType;

public class ReportGenerateRequestDTO {
    private Long generatedByUserId;
    private ReportType reportType;
    private String dateRange;

    public ReportGenerateRequestDTO() {}
    public Long getGeneratedByUserId() { return generatedByUserId; }
    public void setGeneratedByUserId(Long generatedByUserId) { this.generatedByUserId = generatedByUserId; }
    public ReportType getReportType() { return reportType; }
    public void setReportType(ReportType reportType) { this.reportType = reportType; }
    public String getDateRange() { return dateRange; }
    public void setDateRange(String dateRange) { this.dateRange = dateRange; }
}
