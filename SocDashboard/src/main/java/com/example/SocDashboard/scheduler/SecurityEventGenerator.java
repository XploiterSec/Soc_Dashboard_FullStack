//import java.time.LocalDateTime;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import com.example.SocDashboard.entity.SecurityEvent;
//import com.example.SocDashboard.service.SecurityEventService;
//
//@Component
//public class SecurityEventGenerator {
//
//    private final SecurityEventService service;
//
//    public SecurityEventGenerator(SecurityEventService service) {
//        this.service = service;
//    }
//
//    @Scheduled(fixedRate = 5000)
//    public void generateEvent() {
//
//        SecurityEvent event = new SecurityEvent();
//
//        event.setTimestamp(LocalDateTime.now());
//        event.setSourceIp("192.168.1.25");
//        event.setDestinationIp("10.0.0.15");
//        event.setEventType("FAILED_LOGIN");
//        event.setSeverity("HIGH");
//        event.setDescription("Multiple failed login attempts detected");
//        event.setStatus("OPEN");
//
//        service.saveEvent(event);
//    }
//}