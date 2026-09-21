package com.project.lms.controller;

import com.project.lms.entity.Course;
import com.project.lms.repository.CourseRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(
            CourseRepository courseRepository) {

        this.courseRepository = courseRepository;
    }


    // ======================================
    // GET ALL ACTIVE COURSES - STUDENT
    // ======================================

    @GetMapping
    public ResponseEntity<List<Course>> getAvailableCourses() {

        List<Course> courses =
                courseRepository.findByStatus("ACTIVE");

        return ResponseEntity.ok(courses);
    }


    // ======================================
    // GET MENTOR COURSES
    // ======================================

    @GetMapping("/mentor")
    public ResponseEntity<List<Course>> getMentorCourses() {

        /*
         * Temporary mentor ID.
         *
         * We will replace this with
         * the logged-in mentor ID from JWT.
         */

        Long mentorId = 1L;

        List<Course> courses =
                courseRepository.findByMentorId(mentorId);

        return ResponseEntity.ok(courses);
    }


    // ======================================
    // CREATE COURSE
    // ======================================

    @PostMapping
    public ResponseEntity<Course> createCourse(
            @RequestBody Course course) {

        /*
         * Temporary mentor ID.
         *
         * Later this will come from
         * the authenticated JWT user.
         */

        course.setMentorId(1L);

        course.setStatus("ACTIVE");

        Course savedCourse =
                courseRepository.save(course);

        return ResponseEntity.ok(savedCourse);
    }


    // ======================================
    // DELETE COURSE
    // ======================================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(
            @PathVariable Long id) {

        if (!courseRepository.existsById(id)) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        courseRepository.deleteById(id);

        return ResponseEntity.ok(
                "Course deleted successfully"
        );
    }
}