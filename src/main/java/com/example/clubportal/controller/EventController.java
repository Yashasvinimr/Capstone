package com.example.clubportal.controller;

import com.example.clubportal.entity.Event;
import com.example.clubportal.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/explore")
    public ResponseEntity<List<Event>> getExploreEvents() {
        return ResponseEntity.ok(eventService.getExploreEvents());
    }

    @GetMapping("/for-you/{userId}")
    public ResponseEntity<List<Event>> getForYouEvents(@PathVariable Long userId) {
        return ResponseEntity.ok(eventService.getForYouEvents(userId));
    }
    @PostMapping("/{clubId}")
    public ResponseEntity<Event> createEvent(@PathVariable Long clubId, @RequestBody Event event) {
        return ResponseEntity.ok(eventService.createEvent(clubId, event));
    }
}
