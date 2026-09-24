package com.example.catalog.core.course;

import com.example.catalog.api.CourseView;
import com.example.catalog.api.CreateCourseRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseView> create(@RequestBody CreateCourseRequest request) {
        Course course = courseService.create(request.title(), request.capacity(), request.city());
        return ResponseEntity.ok(toView(course));
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<CourseView> publish(@PathVariable Long id) {
        Course course = courseService.publish(id);
        return ResponseEntity.ok(toView(course));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseView> get(@PathVariable Long id) {
        return ResponseEntity.ok(toView(courseService.get(id)));
    }

    private CourseView toView(Course course) {
        return new CourseView(course.getId(), course.getTitle(), course.getCapacity(),
                course.getCity(), course.isPublished());
    }
}