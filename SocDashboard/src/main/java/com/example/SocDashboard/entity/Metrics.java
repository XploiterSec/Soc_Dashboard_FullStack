package com.example.SocDashboard.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Metrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int packetsPerSecond;
    private int totalPackets;
    private int uniqueDevices;
    private LocalDateTime timestamp;

    public Metrics() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPacketsPerSecond() {
        return packetsPerSecond;
    }

    public void setPacketsPerSecond(int packetsPerSecond) {
        this.packetsPerSecond = packetsPerSecond;
    }

    public int getTotalPackets() {
        return totalPackets;
    }

    public void setTotalPackets(int totalPackets) {
        this.totalPackets = totalPackets;
    }

    public int getUniqueDevices() {
        return uniqueDevices;
    }

    public void setUniqueDevices(int uniqueDevices) {
        this.uniqueDevices = uniqueDevices;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
