package com.example.catalog.api;

public record CreateCourseRequest(
        String title,
        int capacity,
        String city
) {
}