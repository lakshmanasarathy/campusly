package com.project.lms.config;

import com.project.lms.repository.UserRepository;
import com.project.lms.security.JwtAuthFilter;
import com.project.lms.security.JwtUtil;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public SecurityConfig(
            JwtUtil jwtUtil,
            UserRepository userRepository) {

        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http) throws Exception {

        JwtAuthFilter jwtFilter =
                new JwtAuthFilter(
                        jwtUtil,
                        userRepository
                );

        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                // =========================
                // FRONTEND PAGES
                // =========================
                .requestMatchers(
                    "/",
                    "/index.html",
                    "/login.html",
                    "/register.html",
                    "/dashboard.html",
                    "/student-home.html",
                    "/favicon.ico"
                ).permitAll()

                // =========================
                // CSS / JS / IMAGES
                // =========================
                .requestMatchers(
                    "/css/**",
                    "/js/**",
                    "/images/**"
                ).permitAll()

                // =========================
                // AUTH APIs
                // =========================
                .requestMatchers(
                    "/api/auth/**"
                ).permitAll()

                // =========================
                // PUBLIC APIs
                // =========================
                .requestMatchers(
                    "/api/public/**"
                ).permitAll()

                // =========================
                // STUDENT APIs
                // =========================
                .requestMatchers(
                    "/api/student/**"
                ).hasRole("STUDENT")

                // =========================
                // OTHER REQUESTS
                // =========================
                .anyRequest().authenticated()
            )

            // =========================
            // JWT FILTER
            // =========================
            .addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}