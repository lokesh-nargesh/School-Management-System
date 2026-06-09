package com.school.management.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

public class Dtos {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResponse {
        private String token;
        private String email;
        private String name;
        private String role;
        private Long id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterRequest {
        private String name;
        private String email;
        private String password;
        private String role; // ADMIN, TEACHER, STUDENT, PARENT
        private String phone;

        // Student/Teacher details
        private String rollNo;
        private String parentName;
        private String parentPhone;
        private Long classId;
        private String specialization;
        private Double salary;
    }
}
