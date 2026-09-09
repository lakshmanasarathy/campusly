package com.project.lms.repository;

import com.project.lms.entity.College;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollegeRepository
        extends JpaRepository<College, Long> {
}