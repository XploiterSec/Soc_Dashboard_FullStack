package com.example.SocDashboard.controller;

import java.util.List;
import java.util.Map;

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

import com.example.SocDashboard.entity.SecurityEvent;
import com.example.SocDashboard.service.SecurityEventService;

@RestController
@RequestMapping("/api/security-events")
@CrossOrigin(origins = "http://localhost:5173")
public class SecurityEventController {

    private final SecurityEventService service;

    public SecurityEventController(SecurityEventService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SecurityEvent> createEvent(@RequestBody SecurityEvent event) {
        SecurityEvent savedEvent = service.saveEvent(event);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SecurityEvent>> getAllEvents() {
        return ResponseEntity.ok(service.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecurityEvent> getEvent(@PathVariable Long id) {
        SecurityEvent event = service.getEventById(id);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(event);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SecurityEvent> updateEvent(@PathVariable Long id, @RequestBody SecurityEvent eventDetails) {
        SecurityEvent updatedEvent = service.updateEvent(id, eventDetails);
        if (updatedEvent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (service.getEventById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        service.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/stats/severity")
    public Map<String, Long> getSeverityCounts() {
        return service.getSeverityCounts();
    }
    
    @GetMapping("/stats/top-source-ips")
    public List<Object[]> getTopSourceIps() {
        return service.getTopSourceIps();
    }
}
