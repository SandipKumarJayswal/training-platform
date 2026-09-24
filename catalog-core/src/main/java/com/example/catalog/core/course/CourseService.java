package com.example.catalog.core.course;

import com.example.catalog.api.CoursePublishedEvent;
import com.example.common.api.DomainEventPublisher;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class CourseService {

    private final CourseRepository repository;
    private final DomainEventPublisher eventPublisher;

    public CourseService(CourseRepository repository, DomainEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    public Course create(String title, int capacity, String city) {
        return repository.save(new Course(title, capacity, city));
    }

    public Course get(Long id) {
        return repository.findById(id).orElseThrow(() -> new CourseNotFoundException(id));
    }

    public Course publish(Long id) {
        Course course = get(id);
        course.publish();
        Course saved = repository.save(course);

        eventPublisher.publish(new CoursePublishedEvent(
                saved.getId(), saved.getTitle(), saved.getCity(), saved.getCapacity(), Instant.now()));

        return saved;
    }
}