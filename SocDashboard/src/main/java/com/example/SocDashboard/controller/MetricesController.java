
package com.example.SocDashboard.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SocDashboard.entity.Metrics;
import com.example.SocDashboard.service.MetricesService;

@RestController
@RequestMapping("/api/metrics")
@CrossOrigin(origins = "http://localhost:5173")
public class MetricesController {

    private final MetricesService service;

    public MetricesController(MetricesService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Metrics> createMetric(
            @RequestBody Metrics metric) {

        Metrics savedMetric = service.saveMetric(metric);

        return new ResponseEntity<>(
                savedMetric,
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Metrics>> getAllMetrics() {

        return ResponseEntity.ok(
                service.getAllMetrics());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Metrics> getMetric(
            @PathVariable Long id) {

        Metrics metric = service.getMetricById(id);

        if (metric == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(metric);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Metrics> updateMetric(
            @PathVariable Long id,
            @RequestBody Metrics metricDetails) {

        Metrics updatedMetric =
                service.updateMetric(id, metricDetails);

        if (updatedMetric == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedMetric);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetric(
            @PathVariable Long id) {

        if (service.getMetricById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        service.deleteMetric(id);

        return ResponseEntity.noContent().build();
    }
}