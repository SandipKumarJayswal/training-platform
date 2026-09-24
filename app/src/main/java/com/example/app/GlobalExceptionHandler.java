package com.example.app;

import com.example.catalog.core.course.CourseNotFoundException;
import com.example.enrollments.core.enrollment.CourseAtCapacityException;
import com.example.enrollments.core.enrollment.CourseNotAvailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<Map<String, String>> notFound(CourseNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(CourseNotAvailableException.class)
    public ResponseEntity<Map<String, String>> notAvailable(CourseNotAvailableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(CourseAtCapacityException.class)
    public ResponseEntity<Map<String, String>> atCapacity(CourseAtCapacityException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
    }
}