package com.example.enrollments.core.enrollment;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "enrolled_at", nullable = false)
    private Instant enrolledAt;

    protected Enrollment() { }

    public Enrollment(String name, String email, Long courseId, Instant enrolledAt) {
        this.name = name;
        this.email = email;
        this.courseId = courseId;
        this.enrolledAt = enrolledAt;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Long getCourseId() { return courseId; }
    public Instant getEnrolledAt() { return enrolledAt; }
}