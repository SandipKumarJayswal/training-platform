package com.example.enrollments.api;

import com.example.common.api.DomainEvent;
import java.time.Instant;

public record EnrollmentCreatedEvent(
        Long enrollmentId,
        String name,
        String email,
        Long courseId,
        Instant enrolledAt
) implements DomainEvent {
}