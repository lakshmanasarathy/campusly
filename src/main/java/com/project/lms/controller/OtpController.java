package com.project.lms.controller;

import com.project.lms.dto.SendOtpRequest;
import com.project.lms.dto.VerifyOtpRequest;
import com.project.lms.service.OtpService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendOtp(
            @Valid @RequestBody SendOtpRequest request) {

        try {
        	otpService.generateAndSendOtp(request.getEmail());
            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "OTP sent successfully. Check your email."
                    )
            );

        } catch (Exception exception) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            Map.of(
                                    "message",
                                    "Unable to send OTP. Please try again later."
                            )
                    );
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {

    	boolean verified = otpService.verifyOtp(
                request.getEmail(),
                request.getOtp()
        );

        if (verified) {
            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "OTP verified successfully."
                    )
            );
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        Map.of(
                                "message",
                                "Invalid or expired OTP."
                        )
                );
    }
}