package com.example.enrollments.core.enrollment;

import com.example.catalog.api.CoursePublishedEvent;
import com.example.common.api.DomainEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CoursePublishedEventHandler implements DomainEventHandler<CoursePublishedEvent> {

    private static final Logger log = LoggerFactory.getLogger(CoursePublishedEventHandler.class);

    @Override
    public Class<CoursePublishedEvent> eventType() {
        return CoursePublishedEvent.class;
    }

    @Override
    public void handle(CoursePublishedEvent event) {
        log.info("enrollments domain: course published courseId={} title='{}' city={} capacity={}",
                event.courseId(), event.title(), event.city(), event.capacity());
    }
}