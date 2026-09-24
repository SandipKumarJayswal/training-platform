package com.example.enrollments.core.enrollment;

public class CourseNotAvailableException extends RuntimeException {
    public CourseNotAvailableException(String message) {
        super(message);
    }
}