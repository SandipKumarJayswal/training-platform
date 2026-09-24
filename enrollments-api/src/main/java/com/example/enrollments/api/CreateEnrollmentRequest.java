package com.example.enrollments.api;

public record CreateEnrollmentRequest(
        String name,
        String email,
        Long courseId
) {
}