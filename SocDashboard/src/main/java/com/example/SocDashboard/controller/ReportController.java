package com.example.SocDashboard.controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SocDashboard.dto.ReportResponse;
import com.example.SocDashboard.dto.ReportRequestDTO;
import com.example.SocDashboard.service.ReportRequestService;
@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:5173")
public class ReportController {

    private final ReportRequestService service;

    public ReportController(ReportRequestService service) {
        this.service = service;
    }

    @PostMapping
    public ReportResponse generateReport(@RequestBody ReportRequestDTO request) {
        return service.generateReport(request);
    }
}