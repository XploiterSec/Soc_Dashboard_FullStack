package com.example.SocDashboard.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.SocDashboard.dto.ReportRequestDTO;
import com.example.SocDashboard.dto.ReportResponse;
import com.example.SocDashboard.entity.SecurityEvent;
import com.example.SocDashboard.repository.SecurityEventRepository;

@Service
public class ReportRequestService {

    private final SecurityEventRepository repository;

    public ReportRequestService(SecurityEventRepository repository) {
        this.repository = repository;
    }

    public ReportResponse generateReport(ReportRequestDTO request) {

        LocalDateTime startTime = calculateStartTime(request.getDateRange());

        List<SecurityEvent> events = repository.findAll();

        long alerts = 0;
        long threats = 0;

        for (SecurityEvent event : events) {

            // Date filter
            if (event.getTimestamp().isBefore(startTime)) {
                continue;
            }

            // Severity filter
            if (!request.getSeverity().equals("All Severity")
                    && !event.getSeverity().equalsIgnoreCase(request.getSeverity())) {
                continue;
            }

            // Matching event = alert
            alerts++;

            // Medium, High and Critical = threat
            if (!event.getSeverity().equalsIgnoreCase("LOW")) {
                threats++;
            }
        }

        ReportResponse response = new ReportResponse(
                request.getReportType() + " Report",
                request.getReportType(),
                LocalDateTime.now().toString(),
                alerts,
                threats,
                "Ready"
        );
        
        return response;
    }

    private LocalDateTime calculateStartTime(String dateRange) {

        LocalDateTime now = LocalDateTime.now();

        return switch (dateRange) {

            case "Last 24 Hours" ->
                    now.minusHours(24);

            case "Last 7 Days" ->
                    now.minusDays(7);

            case "Last 30 Days" ->
                    now.minusDays(30);

            default ->
                    now.minusDays(7);
        };
    }
}