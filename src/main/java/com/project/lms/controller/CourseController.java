package com.project.lms.controller;

import com.project.lms.entity.Course;
import com.project.lms.entity.User;
import com.project.lms.repository.CourseRepository;
import com.project.lms.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin
public class CourseController {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseController(
            CourseRepository courseRepository,
            UserRepository userRepository) {

        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }


    // ======================================
    // GET MENTOR COURSES
    // ======================================

    @GetMapping("/mentor")
    public ResponseEntity<?> getMentorCourses(
            Authentication authentication) {

        try {

            String email = authentication.getName();

            User mentor = userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException("Mentor not found"));

            List<Course> courses =
                    courseRepository.findByMentorId(mentor.getId());

            return ResponseEntity.ok(courses);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }


    // ======================================
    // GET ACTIVE COURSES - STUDENT
    // ======================================

    @GetMapping
    public ResponseEntity<List<Course>> getAvailableCourses() {

        return ResponseEntity.ok(
                courseRepository.findByStatus("ACTIVE")
        );
    }


    // ======================================
    // CREATE COURSE
    // ======================================

    @PostMapping
    public ResponseEntity<?> createCourse(
            @RequestBody Course course,
            Authentication authentication) {

        try {

            String email = authentication.getName();

            User mentor = userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException("Mentor not found"));

            course.setMentorId(mentor.getId());

            course.setStatus("ACTIVE");

            Course savedCourse =
                    courseRepository.save(course);

            return ResponseEntity.ok(savedCourse);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }


    // ======================================
    // DELETE COURSE
    // ======================================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(
            @PathVariable Long id,
            Authentication authentication) {

        try {

            String email = authentication.getName();

            User mentor = userRepository.findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException("Mentor not found"));

            Course course =
                    courseRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException("Course not found"));

            if (!mentor.getId().equals(course.getMentorId())) {

                return ResponseEntity.status(403)
                        .body("You are not the mentor of this course");
            }

            courseRepository.deleteById(id);

            return ResponseEntity.ok(
                    "Course deleted successfully");

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
}