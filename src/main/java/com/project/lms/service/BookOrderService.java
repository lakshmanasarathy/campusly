package com.project.lms.service;

import com.project.lms.entity.Book;
import com.project.lms.entity.BookOrder;
import com.project.lms.repository.BookOrderRepository;
import com.project.lms.repository.BookRepository;
import com.razorpay.Order;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class BookOrderService {

    private final BookRepository bookRepository;
    private final BookOrderRepository bookOrderRepository;
    private final RazorpayService razorpayService;

    public BookOrderService(
            BookRepository bookRepository,
            BookOrderRepository bookOrderRepository,
            RazorpayService razorpayService) {

        this.bookRepository = bookRepository;
        this.bookOrderRepository = bookOrderRepository;
        this.razorpayService = razorpayService;
    }

    public Order createPaymentOrder(Long bookId) throws Exception {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new RuntimeException("Book not found"));

        if (!"AVAILABLE".equals(book.getStatus())) {
            throw new RuntimeException("Book is no longer available");
        }

        Order razorpayOrder =
                razorpayService.createOrder(book.getPrice());

        BookOrder bookOrder = new BookOrder();

        bookOrder.setBookId(book.getId());
        bookOrder.setSellerId(book.getSellerId());
        bookOrder.setAmount(book.getPrice());
        bookOrder.setStatus("CREATED");
        bookOrder.setRazorpayOrderId(
                razorpayOrder.get("id")
        );

        bookOrderRepository.save(bookOrder);

        return razorpayOrder;
    }

    public BookOrder verifyPayment(
            String razorpayOrderId,
            String razorpayPaymentId,
            String razorpaySignature,
            Long bookId) throws Exception {

        String secret = System.getenv("RAZORPAY_KEY_SECRET");

        if (secret == null || secret.isBlank()) {
            throw new RuntimeException(
                    "Razorpay secret key is not configured"
            );
        }

        String payload =
                razorpayOrderId + "|" + razorpayPaymentId;

        boolean valid = Utils.verifySignature(
                payload,
                razorpaySignature,
                secret
        );

        if (!valid) {
            throw new RuntimeException(
                    "Invalid Razorpay payment signature"
            );
        }

        BookOrder order =
                bookOrderRepository
                        .findByRazorpayOrderId(razorpayOrderId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Book order not found"
                                ));

        order.setRazorpayPaymentId(
                razorpayPaymentId
        );

        order.setStatus("PAID");

        Book book =
                bookRepository.findById(bookId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Book not found"
                                ));

        book.setStatus("SOLD");

        bookRepository.save(book);

        return bookOrderRepository.save(order);
    }
}