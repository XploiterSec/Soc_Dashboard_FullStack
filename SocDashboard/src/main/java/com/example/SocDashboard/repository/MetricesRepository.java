
package com.example.SocDashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SocDashboard.entity.Metrics;

public interface MetricesRepository extends JpaRepository<Metrics, Long> {

}