package com.example.SocDashboard.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.SocDashboard.entity.Metrics;
import com.example.SocDashboard.repository.MetricesRepository;

@Service
public class MetricesService {

    private final MetricesRepository repository;

    public MetricesService(MetricesRepository repository) {
        this.repository = repository;
    }

    public Metrics saveMetric(Metrics metric) {
        return repository.save(metric);
    }

    public List<Metrics> getAllMetrics() {
        return repository.findAll();
    }

    public Metrics getMetricById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Metrics updateMetric(Long id, Metrics metricDetails) {

        Metrics existingMetric = repository.findById(id).orElse(null);

        if (existingMetric == null) {
            return null;
        }

        existingMetric.setPacketsPerSecond(
                metricDetails.getPacketsPerSecond());

        existingMetric.setTotalPackets(
                metricDetails.getTotalPackets());

        existingMetric.setUniqueDevices(
                metricDetails.getUniqueDevices());

        existingMetric.setTimestamp(
                metricDetails.getTimestamp());

        return repository.save(existingMetric);
    }

    public void deleteMetric(Long id) {
        repository.deleteById(id);
    }
}
