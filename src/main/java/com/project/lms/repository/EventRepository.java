package com.project.lms.repository;

import com.project.lms.entity.Event;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository
        extends JpaRepository<Event, Long> {
}