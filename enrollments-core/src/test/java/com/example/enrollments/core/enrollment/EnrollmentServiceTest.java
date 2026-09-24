package com.example.enrollments.core.enrollment;

import com.example.catalog.api.CourseView;
import com.example.catalog.api.CoursesApi;
import com.example.common.api.DomainEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EnrollmentServiceTest {

    private final EnrollmentRepository repository = mock(EnrollmentRepository.class);
    private final CoursesApi coursesApi = mock(CoursesApi.class);
    private final DomainEventPublisher eventPublisher = mock(DomainEventPublisher.class);

    private EnrollmentService service;

    @BeforeEach
    void setUp() {
        service = new EnrollmentService(repository, coursesApi, eventPublisher);
    }

    @Test
    void rejectsEnrollmentWhenCourseDoesNotExist() {
        when(coursesApi.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.enroll("Jane", "jane@example.com", 99L))
                .isInstanceOf(CourseNotAvailableException.class);
    }

    @Test
    void rejectsEnrollmentWhenCourseIsNotPublished() {
        when(coursesApi.findById(1L)).thenReturn(Optional.of(
                new CourseView(1L, "Java Basics", 10, "Berlin", false)));

        assertThatThrownBy(() -> service.enroll("Jane", "jane@example.com", 1L))
                .isInstanceOf(CourseNotAvailableException.class);
    }

    @Test
    void rejectsEnrollmentWhenCourseIsAtCapacity() {
        when(coursesApi.findById(1L)).thenReturn(Optional.of(
                new CourseView(1L, "Java Basics", 1, "Berlin", true)));
        when(repository.countByCourseId(1L)).thenReturn(1L);

        assertThatThrownBy(() -> service.enroll("Jane", "jane@example.com", 1L))
                .isInstanceOf(CourseAtCapacityException.class);
    }

    @Test
    void enrollsAndPublishesEventWhenThereIsRoom() {
        when(coursesApi.findById(1L)).thenReturn(Optional.of(
                new CourseView(1L, "Java Basics", 2, "Berlin", true)));
        when(repository.countByCourseId(1L)).thenReturn(0L);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Enrollment result = service.enroll("Jane", "jane@example.com", 1L);

        assertThat(result.getName()).isEqualTo("Jane");
        verify(eventPublisher).publish(any());
    }
}