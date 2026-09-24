package com.example.enrollments.core.enrollment;

import com.example.enrollments.api.CreateEnrollmentRequest;
import com.example.enrollments.api.EnrollmentView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public ResponseEntity<EnrollmentView> enroll(@RequestBody CreateEnrollmentRequest request) {
        Enrollment enrollment = enrollmentService.enroll(request.name(), request.email(), request.courseId());
        return ResponseEntity.ok(toView(enrollment));
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentView>> listForCourse(@RequestParam Long courseId) {
        List<EnrollmentView> views = enrollmentService.listByCourse(courseId).stream()
                .map(this::toView)
                .toList();
        return ResponseEntity.ok(views);
    }

    private EnrollmentView toView(Enrollment enrollment) {
        return new EnrollmentView(enrollment.getId(), enrollment.getName(), enrollment.getEmail(),
                enrollment.getCourseId(), enrollment.getEnrolledAt());
    }
}