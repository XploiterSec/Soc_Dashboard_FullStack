package com.example.SocDashboard.dto;

public class ReportResponse {

    private String name;
    private String type;
    private String date;
    private long alerts;
    private long threats;
    private String status;

    public ReportResponse() {
    }

    public ReportResponse(String name, String type, String date,
                          long alerts, long threats, String status) {
        this.name = name;
        this.type = type;
        this.date = date;
        this.alerts = alerts;
        this.threats = threats;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public long getAlerts() {
        return alerts;
    }

    public long getThreats() {
        return threats;
    }

    public String getStatus() {
        return status;
    }
}