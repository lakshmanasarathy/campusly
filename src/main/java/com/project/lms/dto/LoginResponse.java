package com.project.lms.dto;

import com.project.lms.entity.Role;

public class LoginResponse {

    private String token;
    private String email;
    private String fullName;
    private Role role;

    public LoginResponse(
            String token,
            String email,
            String fullName,
            Role role) {

        this.token = token;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public Role getRole() {
        return role;
    }
}