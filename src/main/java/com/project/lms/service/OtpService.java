package com.project.lms.service;

import com.project.lms.entity.OtpVerification;
import com.project.lms.repository.OtpVerificationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Optional;

@Service
public class OtpService {

    private final OtpVerificationRepository otpRepository;
    private final EmailService emailService;
    private final SecureRandom secureRandom = new SecureRandom();

    public OtpService(
            OtpVerificationRepository otpRepository,
            EmailService emailService) {

        this.otpRepository = otpRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void generateAndSendOtp(String email) {

        String normalizedEmail = normalizeEmail(email);

        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1_000_000)
        );

        String otpHash = hashOtp(normalizedEmail, otp);

        LocalDateTime now = LocalDateTime.now();

        OtpVerification verification = otpRepository
                .findByEmail(normalizedEmail)
                .orElseGet(OtpVerification::new);

        verification.setEmail(normalizedEmail);
        verification.setOtpHash(otpHash);
        verification.setExpiryTime(now.plusMinutes(5));
        verification.setCreatedAt(now);
        verification.setAttempts(0);

        otpRepository.saveAndFlush(verification);

        // Send the OTP only after the database write succeeds.
        // If sending fails, the endpoint must report failure.
        emailService.sendOtpEmail(normalizedEmail, otp);
    }

    @Transactional
    public boolean verifyOtp(String email, String enteredOtp) {

    	if (email == null || enteredOtp == null ||
    	        !enteredOtp.matches("\\d{6}")) {
    	    return false;
    	}

        String normalizedEmail = normalizeEmail(email);

        Optional<OtpVerification> optional =
                otpRepository.findByEmail(normalizedEmail);

        if (optional.isEmpty()) {
            return false;
        }

        OtpVerification verification = optional.get();

        if (verification.getExpiryTime().isBefore(LocalDateTime.now()) ||
                verification.getExpiryTime().isEqual(LocalDateTime.now())) {

            otpRepository.delete(verification);
            return false;
        }

        if (verification.getAttempts() >= 5) {
            otpRepository.delete(verification);
            return false;
        }

        String enteredHash = hashOtp(normalizedEmail, enteredOtp);

        if (!MessageDigest.isEqual(
                verification.getOtpHash().getBytes(StandardCharsets.UTF_8),
                enteredHash.getBytes(StandardCharsets.UTF_8))) {

            verification.setAttempts(verification.getAttempts() + 1);
            otpRepository.save(verification);

            return false;
        }

        otpRepository.delete(verification);

        return true;
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        return email.trim().toLowerCase(java.util.Locale.ROOT);
    }

    private String hashOtp(String email, String otp) {

        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            String value = email + ":" + otp;

            byte[] hash = digest.digest(
                    value.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "Unable to hash OTP",
                    exception
            );
        }
    }
}