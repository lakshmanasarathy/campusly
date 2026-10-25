package com.project.lms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "otp_verification",
    indexes = {
        @Index(name = "idx_otp_email", columnList = "email")
    }
)
public class OtpVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 254)
    private String email;

    @Column(nullable = false, length = 64)
    private String otpHash;

    @Column(nullable = false)
    private LocalDateTime expiryTime;

    @Column(nullable = false)
    private int attempts = 0;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public OtpVerification() {
    }

    public OtpVerification(
            String email,
            String otpHash,
            LocalDateTime expiryTime,
            LocalDateTime createdAt) {

        this.email = email;
        this.otpHash = otpHash;
        this.expiryTime = expiryTime;
        this.createdAt = createdAt;
        this.attempts = 0;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getOtpHash() {
        return otpHash;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public int getAttempts() {
        return attempts;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOtpHash(String otpHash) {
        this.otpHash = otpHash;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}