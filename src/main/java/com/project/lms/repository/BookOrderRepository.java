package com.project.lms.repository;

import com.project.lms.entity.BookOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookOrderRepository extends JpaRepository<BookOrder, Long> {

    Optional<BookOrder> findByRazorpayOrderId(String razorpayOrderId);
}