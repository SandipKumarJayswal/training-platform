package com.example.catalog.api;

import com.example.common.api.DomainEvent;
import java.time.Instant;

public record CoursePublishedEvent(
        Long courseId,
        String title,
        String city,
        int capacity,
        Instant publishedAt
) implements DomainEvent {
}