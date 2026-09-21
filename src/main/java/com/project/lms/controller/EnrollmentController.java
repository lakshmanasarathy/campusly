package com.project.lms.controller;

import com.project.lms.entity.Course;
import com.project.lms.entity.Enrollment;
import com.project.lms.entity.User;
import com.project.lms.repository.CourseRepository;
import com.project.lms.repository.EnrollmentRepository;
import com.project.lms.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin
public class EnrollmentController {

    private final EnrollmentRepository enrollmentRepository;

    private final UserRepository userRepository;

    private final CourseRepository courseRepository;


    public EnrollmentController(
            EnrollmentRepository enrollmentRepository,
            UserRepository userRepository,
            CourseRepository courseRepository) {

        this.enrollmentRepository =
                enrollmentRepository;

        this.userRepository =
                userRepository;

        this.courseRepository =
                courseRepository;
    }

    @PostMapping("/{courseId}")
    public ResponseEntity<?> enroll(
            @PathVariable Long courseId,
            Authentication authentication) {

        try {
            String email =
                    authentication.getName();

            User student =
                    userRepository
                            .findByEmail(email)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Student not found"
                                    ));

            Course course =
                    courseRepository
                            .findById(courseId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Course not found"
                                    ));

            if (!"ACTIVE".equals(course.getStatus())) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "This course is not available"
                        );
            }

            boolean alreadyEnrolled =
                    enrollmentRepository
                            .existsByCourseIdAndStudentId(
                                    courseId,
                                    student.getId()
                            );


            if (alreadyEnrolled) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "You are already enrolled in this course"
                        );
            }


            Enrollment enrollment =
                    new Enrollment();

            enrollment.setCourseId(courseId);

            enrollment.setStudentId(
                    student.getId()
            );

            enrollment.setEnrolledAt(
                    LocalDateTime.now()
            );


            enrollmentRepository.save(
                    enrollment
            );


            return ResponseEntity.ok(
                    "Successfully enrolled in "
                    + course.getTitle()
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/my-courses")
    public ResponseEntity<?> getMyCourses(
            Authentication authentication) {

        try {

            String email =
                    authentication.getName();


            User student =
                    userRepository
                            .findByEmail(email)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Student not found"
                                    ));


            List<Enrollment> enrollments =
                    enrollmentRepository
                            .findByStudentId(
                                    student.getId()
                            );


            List<Course> courses =
                    new ArrayList<>();


            for (Enrollment enrollment :
                    enrollments) {

                courseRepository
                        .findById(
                                enrollment.getCourseId()
                        )
                        .ifPresent(courses::add);
            }


            return ResponseEntity.ok(courses);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getEnrolledStudents(
            @PathVariable Long courseId,
            Authentication authentication) {

        try {

            String email =
                    authentication.getName();


            User mentor =
                    userRepository
                            .findByEmail(email)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Mentor not found"
                                    ));


            Course course =
                    courseRepository
                            .findById(courseId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Course not found"
                                    ));

            if (!mentor.getId().equals(
                    course.getMentorId())) {

                return ResponseEntity
                        .status(403)
                        .body(
                                "You are not the mentor of this course"
                        );
            }


            List<Enrollment> enrollments =
                    enrollmentRepository
                            .findByCourseId(courseId);


            List<Map<String, Object>> students =
                    new ArrayList<>();


            for (Enrollment enrollment :
                    enrollments) {

                User student =
                        userRepository
                                .findById(
                                        enrollment.getStudentId()
                                )
                                .orElse(null);


                if (student != null) {

                    Map<String, Object> data =
                            new HashMap<>();

                    data.put(
                            "id",
                            student.getId()
                    );

                    data.put(
                            "fullName",
                            student.getFullName()
                    );

                    data.put(
                            "email",
                            student.getEmail()
                    );

                    data.put(
                            "enrolledAt",
                            enrollment.getEnrolledAt()
                    );

                    students.add(data);
                }
            }


            return ResponseEntity.ok(students);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/course/{courseId}/count")
    public ResponseEntity<?> getEnrollmentCount(
            @PathVariable Long courseId,
            Authentication authentication) {

        try {

            String email =
                    authentication.getName();


            User mentor =
                    userRepository
                            .findByEmail(email)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Mentor not found"
                                    ));


            Course course =
                    courseRepository
                            .findById(courseId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Course not found"
                                    ));

            if (!mentor.getId().equals(
                    course.getMentorId())) {

                return ResponseEntity
                        .status(403)
                        .body(
                                "You are not the mentor of this course"
                        );
            }


            long count =
                    enrollmentRepository
                            .countByCourseId(courseId);


            Map<String, Object> response =
                    new HashMap<>();

            response.put(
                    "courseId",
                    courseId
            );

            response.put(
                    "count",
                    count
            );


            return ResponseEntity.ok(
                    response
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}