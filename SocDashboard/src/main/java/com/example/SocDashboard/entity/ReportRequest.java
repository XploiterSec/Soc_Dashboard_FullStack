package com.example.SocDashboard.entity;

public class ReportRequest {

    private String reportType;
    private String dateRange;
    private String severity;
    
    public ReportRequest() {
    	
    }

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getDateRange() {
		return dateRange;
	}

	public void setDateRange(String dateRange) {
		this.dateRange = dateRange;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}
    
    
}