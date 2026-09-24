package com.example.catalog.api;

import java.util.Optional;

public interface CoursesApi {
    Optional<CourseView> findById(Long id);
}