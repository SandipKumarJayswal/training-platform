package com.example.catalog.api;

import java.util.Optional;

public interface CoursesApi {
	// sample comment
    Optional<CourseView> findById(Long id);
}