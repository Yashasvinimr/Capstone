package com.example.clubportal.controller;

import com.example.clubportal.entity.EventRegistration;
import com.example.clubportal.service.EventRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/event-registrations")
@RequiredArgsConstructor
public class EventRegistrationController {
    private final EventRegistrationService eventRegistrationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EventRegistration>> getUserRegistrations(@PathVariable Long userId) {
        return ResponseEntity.ok(eventRegistrationService.getUserRegistrations(userId));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<EventRegistration>> getEventRegistrations(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventRegistrationService.getEventRegistrations(eventId));
    }

    @PostMapping("/{userId}/{eventId}")
    public ResponseEntity<EventRegistration> registerForEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return ResponseEntity.ok(eventRegistrationService.registerForEvent(userId, eventId));
    }

    @DeleteMapping("/{userId}/{eventId}")
    public ResponseEntity<Void> cancelRegistration(@PathVariable Long userId, @PathVariable Long eventId) {
        eventRegistrationService.cancelRegistration(userId, eventId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/complete-payment/{userId}/{eventId}")
    public ResponseEntity<Map<String, String>> completePayment(@PathVariable Long userId, @PathVariable Long eventId) {
        eventRegistrationService.completePayment(userId, eventId);

        // Creating a response map
        Map<String, String> response = new HashMap<>();
        response.put("message", "Payment completed successfully");
        response.put("status", "SUCCESS");

        return ResponseEntity.ok(response);
    }

}
