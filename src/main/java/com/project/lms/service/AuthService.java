package com.project.lms.service;

import com.project.lms.dto.LoginRequest;
import com.project.lms.dto.LoginResponse;
import com.project.lms.dto.RegisterRequest;
import com.project.lms.entity.Role;
import com.project.lms.entity.User;
import com.project.lms.repository.UserRepository;
import com.project.lms.security.JwtUtil;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder;


    public AuthService(
            UserRepository userRepository,
            JwtUtil jwtUtil,
            BCryptPasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;

        this.jwtUtil = jwtUtil;

        this.passwordEncoder = passwordEncoder;
    }


    // ===============================
    // REGISTER
    // ===============================

    public String register(RegisterRequest request) {

        // Check whether email already exists

        if (userRepository.existsByEmail(
                request.getEmail())) {

            throw new RuntimeException(
                    "Email already in use"
            );
        }


        // Create new user

        User user = new User();


        user.setEmail(
                request.getEmail()
        );


        user.setPasswordHash(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );


        user.setFullName(
                request.getFullName()
        );


        // ===============================
        // SET ROLE
        // ===============================

        if (request.getRole() != null) {

            user.setRole(
                    request.getRole()
            );

        } else {

            // Default role

            user.setRole(
                    Role.STUDENT
            );
        }


        user.setCollegeId(
                request.getCollegeId()
        );


        // Save user

        userRepository.save(user);


        return "Registered successfully";
    }


    // ===============================
    // LOGIN
    // ===============================

    public LoginResponse login(
            LoginRequest request) {

        User user =
                userRepository
                        .findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid credentials"
                                )
                        );


        // Check password

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash())) {

            throw new RuntimeException(
                    "Invalid credentials"
            );
        }


        // Generate JWT

        String token =
                jwtUtil.generateToken(
                        user.getEmail()
                );


        // Return login response

        return new LoginResponse(
                token,
                user.getEmail(),
                user.getFullName(),
                user.getRole()
        );
    }
}