package com.example.catalog.core.course;

import com.example.catalog.api.CourseView;
import com.example.catalog.api.CoursesApi;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CourseApiImpl implements CoursesApi {

    private final CourseRepository repository;

    public CourseApiImpl(CourseRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<CourseView> findById(Long id) {
        return repository.findById(id)
                .map(c -> new CourseView(c.getId(), c.getTitle(), c.getCapacity(), c.getCity(), c.isPublished()));
    }
}