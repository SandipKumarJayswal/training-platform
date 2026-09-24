package com.example.enrollments.core.enrollment;

public class CourseAtCapacityException extends RuntimeException {
    public CourseAtCapacityException(Long courseId) {
        super("Course is at capacity: " + courseId);
    }
}	