package com.project.lms.controller;

import com.project.lms.dto.CreatePaymentOrderResponse;
import com.project.lms.dto.PaymentVerificationRequest;
import com.project.lms.entity.Book;
import com.project.lms.entity.BookOrder;
import com.project.lms.repository.BookOrderRepository;
import com.project.lms.repository.BookRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/book-payment")
public class BookPaymentController {

    private final RazorpayClient razorpayClient;
    private final BookRepository bookRepository;
    private final BookOrderRepository bookOrderRepository;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    public BookPaymentController(
            RazorpayClient razorpayClient,
            BookRepository bookRepository,
            BookOrderRepository bookOrderRepository) {

        this.razorpayClient = razorpayClient;
        this.bookRepository = bookRepository;
        this.bookOrderRepository = bookOrderRepository;
    }

    // =====================================================
    // CREATE RAZORPAY ORDER
    // =====================================================

    @PostMapping("/create-order/{bookId}")
    public ResponseEntity<?> createOrder(@PathVariable Long bookId) {

        try {

            // Find book
            Book book = bookRepository.findById(bookId)
                    .orElse(null);

            if (book == null) {
                return ResponseEntity
                        .notFound()
                        .build();
            }

            // Check book availability
            if (!"AVAILABLE".equals(book.getStatus())) {

                return ResponseEntity
                        .badRequest()
                        .body("Book is no longer available");
            }

            // Convert rupees to paise
            long amountInPaise =
                    Math.round(book.getPrice() * 100);

            // Create Razorpay order request
            JSONObject orderRequest =
                    new JSONObject();

            orderRequest.put(
                    "amount",
                    amountInPaise
            );

            orderRequest.put(
                    "currency",
                    "INR"
            );

            orderRequest.put(
                    "receipt",
                    "BOOK_" + bookId
            );

            // Create Razorpay order
            Order razorpayOrder =
                    razorpayClient.orders.create(
                            orderRequest
                    );

            // Create our database order
            BookOrder bookOrder =
                    new BookOrder();

            bookOrder.setBookId(
                    book.getId()
            );

            bookOrder.setSellerId(
                    book.getSellerId()
            );

            bookOrder.setAmount(
                    book.getPrice()
            );

            bookOrder.setStatus(
                    "CREATED"
            );

            bookOrder.setRazorpayOrderId(
                    razorpayOrder.get("id")
            );

            bookOrderRepository.save(
                    bookOrder
            );

            // Send order details to frontend
            CreatePaymentOrderResponse response =
                    new CreatePaymentOrderResponse(

                            razorpayOrder.get("id"),

                            razorpayKeyId,

                            amountInPaise,

                            "INR",

                            bookId
                    );

            return ResponseEntity.ok(
                    response
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body(
                            "Unable to create payment order"
                    );
        }
    }


    // =====================================================
    // VERIFY RAZORPAY PAYMENT
    // =====================================================

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(
            @RequestBody PaymentVerificationRequest request) {

        try {

            // ---------------------------------------------
            // 1. Validate request
            // ---------------------------------------------

            if (request.getRazorpayOrderId() == null ||
                request.getRazorpayPaymentId() == null ||
                request.getRazorpaySignature() == null ||
                request.getBookId() == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Invalid payment verification data");
            }


            // ---------------------------------------------
            // 2. Find our database order
            // ---------------------------------------------

            BookOrder bookOrder =
                    bookOrderRepository
                            .findByRazorpayOrderId(
                                    request.getRazorpayOrderId()
                            )
                            .orElse(null);

            if (bookOrder == null) {

                return ResponseEntity
                        .badRequest()
                        .body("Order not found");
            }


            // ---------------------------------------------
            // 3. Check book ID
            // ---------------------------------------------

            if (!bookOrder.getBookId()
                    .equals(request.getBookId())) {

                return ResponseEntity
                        .badRequest()
                        .body("Book does not match the order");
            }


            // ---------------------------------------------
            // 4. Create Razorpay signature payload
            // ---------------------------------------------

            String payload =
                    request.getRazorpayOrderId()
                    + "|"
                    + request.getRazorpayPaymentId();


            // ---------------------------------------------
            // 5. Verify Razorpay signature
            // ---------------------------------------------

            boolean signatureValid =
                    Utils.verifySignature(
                            payload,
                            request.getRazorpaySignature(),
                            razorpayKeySecret
                    );


            // ---------------------------------------------
            // 6. Reject invalid payment
            // ---------------------------------------------

            if (!signatureValid) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "Invalid payment signature"
                        );
            }


            // ---------------------------------------------
            // 7. Find book
            // ---------------------------------------------

            Book book =
                    bookRepository
                            .findById(
                                    request.getBookId()
                            )
                            .orElse(null);

            if (book == null) {

                return ResponseEntity
                        .notFound()
                        .build();
            }


            // ---------------------------------------------
            // 8. Check if book is still available
            // ---------------------------------------------

            if (!"AVAILABLE".equals(
                    book.getStatus())) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "Book is already sold"
                        );
            }


            // ---------------------------------------------
            // 9. Update BookOrder
            // ---------------------------------------------

            bookOrder.setRazorpayPaymentId(
                    request.getRazorpayPaymentId()
            );

            bookOrder.setStatus(
                    "PAID"
            );


            // ---------------------------------------------
            // 10. Mark book as SOLD
            // ---------------------------------------------

            book.setStatus(
                    "SOLD"
            );


            // ---------------------------------------------
            // 11. Save changes
            // ---------------------------------------------

            bookOrderRepository.save(
                    bookOrder
            );

            bookRepository.save(
                    book
            );


            // ---------------------------------------------
            // 12. Send success response
            // ---------------------------------------------

            Map<String, Object> response =
                    new HashMap<>();

            response.put(
                    "message",
                    "Payment verified successfully"
            );

            response.put(
                    "status",
                    "PAID"
            );

            response.put(
                    "bookId",
                    book.getId()
            );

            response.put(
                    "paymentId",
                    request.getRazorpayPaymentId()
            );

            return ResponseEntity.ok(
                    response
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body(
                            "Payment verification failed"
                    );
        }
    }
}