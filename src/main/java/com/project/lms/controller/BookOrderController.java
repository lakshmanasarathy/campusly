package com.project.lms.controller;

import com.project.lms.dto.CreateOrderResponse;
import com.project.lms.dto.PaymentVerificationRequest;
import com.project.lms.entity.BookOrder;
import com.project.lms.service.BookOrderService;
import com.razorpay.Order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book-payment")
@CrossOrigin
public class BookOrderController {

    private final BookOrderService bookOrderService;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    public BookOrderController(BookOrderService bookOrderService) {
        this.bookOrderService = bookOrderService;
    }

    @PostMapping("/create-order/{bookId}")
    public ResponseEntity<?> createOrder(
            @PathVariable Long bookId) {

        try {

            Order order =
                    bookOrderService.createPaymentOrder(bookId);

            CreateOrderResponse response =
                    new CreateOrderResponse(
                            (String) order.get("id"),
                            razorpayKeyId,
                            ((Number) order.get("amount")).longValue(),
                            (String) order.get("currency")
                    );

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(
            @RequestBody PaymentVerificationRequest request) {

        try {

            BookOrder order =
                    bookOrderService.verifyPayment(
                            request.getRazorpayOrderId(),
                            request.getRazorpayPaymentId(),
                            request.getRazorpaySignature(),
                            request.getBookId()
                    );

            return ResponseEntity.ok(order);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}