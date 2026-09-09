package com.project.lms.controller;

import com.project.lms.entity.Event;
import com.project.lms.service.EventService;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class EventController {

    private final EventService eventService;


    public EventController(EventService eventService) {

        this.eventService = eventService;
    }


    // =========================
    // GET ALL EVENTS
    // =========================

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {

        return ResponseEntity.ok(
                eventService.getAllEvents()
        );
    }


    // =========================
    // CREATE EVENT
    // =========================

    @PostMapping
    public ResponseEntity<?> createEvent(
            @RequestBody Event event,
            Principal principal) {

        try {

            String email =
                    principal.getName();


            Event savedEvent =
                    eventService.createEvent(
                            event,
                            email
                    );


            return ResponseEntity.ok(
                    savedEvent
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // =========================
    // GET EVENT BY ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(
            @PathVariable Long id) {

        try {

            return ResponseEntity.ok(
                    eventService.getEventById(id)
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .notFound()
                    .build();
        }
    }


    // =========================
    // DELETE EVENT
    // =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(
            @PathVariable Long id) {

        try {

            eventService.deleteEvent(id);

            return ResponseEntity.ok(
                    "Event deleted successfully"
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .notFound()
                    .build();
        }
    }
}