package com.example.SocDashboard.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.SocDashboard.entity.SecurityEvent;
import com.example.SocDashboard.repository.SecurityEventRepository;

@Service
public class SecurityEventService {

    private final SecurityEventRepository repository;

    public SecurityEventService(SecurityEventRepository repository) {
        this.repository = repository;
    }

    public SecurityEvent saveEvent(SecurityEvent event) {
        return repository.save(event);
    }

    public List<SecurityEvent> getAllEvents() {
        return repository.findAll();
    }

    public SecurityEvent getEventById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public SecurityEvent updateEvent(Long id, SecurityEvent eventDetails) {
        SecurityEvent existingEvent = repository.findById(id).orElse(null);
        if (existingEvent == null) {
            return null; 
         }
        
        //existingEvent.setTitle(eventDetails.getTitle());
        existingEvent.setDescription(eventDetails.getDescription());
        existingEvent.setSeverity(eventDetails.getSeverity());
        existingEvent.setStatus(eventDetails.getStatus());
        existingEvent.setTimestamp(eventDetails.getTimestamp());
        
        return repository.save(existingEvent);
    }

    public void deleteEvent(Long id) {
        repository.deleteById(id);
    }
    
    public Map<String, Long> getSeverityCounts() {

        Map<String, Long> counts = new HashMap<>();

        counts.put("LOW", repository.countBySeverity("LOW"));
        counts.put("MEDIUM", repository.countBySeverity("MEDIUM"));
        counts.put("HIGH", repository.countBySeverity("HIGH"));
        counts.put("CRITICAL", repository.countBySeverity("CRITICAL"));

        return counts;
    }
    
    public List<Object[]> getTopSourceIps() {
        return repository.findTopSourceIps();
    }
}
