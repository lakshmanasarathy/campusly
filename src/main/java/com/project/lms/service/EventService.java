package com.project.lms.service;

import com.project.lms.entity.Event;
import com.project.lms.entity.User;
import com.project.lms.repository.EventRepository;
import com.project.lms.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;


    public EventService(
            EventRepository eventRepository,
            UserRepository userRepository) {

        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public List<Event> getAllEvents() {

        return eventRepository.findAll();
    }

    public Event createEvent(
            Event event,
            String email) {


        // Find logged-in user
        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );


        // Store creator ID
        event.setCreatedBy(
                user.getId()
        );


        // Save event
        return eventRepository.save(event);
    }

    public Event getEventById(Long id) {

        return eventRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Event not found"
                        )
                );
    }


    public void deleteEvent(Long id) {

        if (!eventRepository.existsById(id)) {

            throw new RuntimeException(
                    "Event not found"
            );
        }

        eventRepository.deleteById(id);
    }
}