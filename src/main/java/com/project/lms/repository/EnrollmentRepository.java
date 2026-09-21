package com.project.lms.repository;

import com.project.lms.entity.Enrollment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository
        extends JpaRepository<Enrollment, Long> {

    boolean existsByCourseIdAndStudentId(
            Long courseId,
            Long studentId
    );

    List<Enrollment> findByCourseId(
            Long courseId
    );

    List<Enrollment> findByStudentId(
            Long studentId
    );

    long countByCourseId(
            Long courseId
    );
}