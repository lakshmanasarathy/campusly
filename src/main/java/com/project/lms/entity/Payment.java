package com.project.lms.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookId;

    private Long buyerId;

    private Long sellerId;

    private Double amount;

    private String currency;

    private String status;

    private String razorpayOrderId;

    private String razorpayPaymentId;

    private String razorpaySignature;

    private LocalDateTime createdAt;

    public Payment() {
    	
    }
    
    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getBookId() {
        return bookId;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public Double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getStatus() {
        return status;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public String getRazorpaySignature() {
        return razorpaySignature;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public void setRazorpaySignature(String razorpaySignature) {
        this.razorpaySignature = razorpaySignature;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}