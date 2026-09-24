package com.example.enrollments.core.enrollment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    long countByCourseId(Long courseId);
    List<Enrollment> findByCourseId(Long courseId);
}