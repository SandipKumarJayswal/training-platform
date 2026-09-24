package com.example.catalog.api;

public record CourseView(
        Long id,
        String title,
        int capacity,
        String city,
        boolean published
) {
}