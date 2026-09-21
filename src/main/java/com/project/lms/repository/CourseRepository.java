package com.project.lms.repository;

import com.project.lms.entity.Course;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository
        extends JpaRepository<Course, Long> {

    List<Course> findByMentorId(Long mentorId);

    List<Course> findByStatus(String status);
}