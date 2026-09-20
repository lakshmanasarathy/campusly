package com.project.lms.repository;

import com.project.lms.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByStatus(String status);

    List<Book> findBySellerId(Long sellerId);
}