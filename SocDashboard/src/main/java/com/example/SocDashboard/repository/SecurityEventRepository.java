package com.example.SocDashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.SocDashboard.entity.SecurityEvent;

public interface SecurityEventRepository extends JpaRepository<SecurityEvent, Long>{
	long countBySeverity(String severity);
	
	@Query("""
		    SELECT s.sourceIp, COUNT(s)
		    FROM SecurityEvent s
		    GROUP BY s.sourceIp
		    ORDER BY COUNT(s) DESC
		""")
		List<Object[]> findTopSourceIps();
}
