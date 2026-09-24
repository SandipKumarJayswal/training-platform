package com.example.enrollments.core.enrollment;

import com.example.catalog.api.CourseView;
import com.example.catalog.api.CoursesApi;
import com.example.common.api.DomainEventPublisher;
import com.example.enrollments.api.EnrollmentCreatedEvent;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository repository;
    private final CoursesApi coursesApi;
    private final DomainEventPublisher eventPublisher;

    public EnrollmentService(EnrollmentRepository repository, CoursesApi coursesApi,
                              DomainEventPublisher eventPublisher) {
        this.repository = repository;
        this.coursesApi = coursesApi;
        this.eventPublisher = eventPublisher;
    }

    public Enrollment enroll(String name, String email, Long courseId) {
        CourseView course = coursesApi.findById(courseId)
                .orElseThrow(() -> new CourseNotAvailableException("Course not found: " + courseId));

        if (!course.published()) {
            throw new CourseNotAvailableException("Course is not published: " + courseId);
        }

        long currentEnrollments = repository.countByCourseId(courseId);
        if (currentEnrollments >= course.capacity()) {
            throw new CourseAtCapacityException(courseId);
        }

        Enrollment saved = repository.save(new Enrollment(name, email, courseId, Instant.now()));

        eventPublisher.publish(new EnrollmentCreatedEvent(
                saved.getId(), saved.getName(), saved.getEmail(), saved.getCourseId(), saved.getEnrolledAt()));

        return saved;
    }

    public List<Enrollment> listByCourse(Long courseId) {
        return repository.findByCourseId(courseId);
    }
}