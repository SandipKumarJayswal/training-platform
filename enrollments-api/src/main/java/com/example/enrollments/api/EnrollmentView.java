package com.example.enrollments.api;

import java.time.Instant;

public record EnrollmentView(
        Long id,
        String name,
        String email,
        Long courseId,
        Instant enrolledAt
) {
}