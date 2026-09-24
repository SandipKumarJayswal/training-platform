package com.example.notifications.core.notification;

import com.example.common.api.DomainEventHandler;
import com.example.enrollments.api.EnrollmentCreatedEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class EnrollmentCreatedEventHandler implements DomainEventHandler<EnrollmentCreatedEvent> {

    private final NotificationRepository repository;
    private final List<NotificationSender> senders;

    public EnrollmentCreatedEventHandler(NotificationRepository repository, List<NotificationSender> senders) {
        this.repository = repository;
        this.senders = senders;
    }

    @Override
    public Class<EnrollmentCreatedEvent> eventType() {
        return EnrollmentCreatedEvent.class;
    }

    @Override
    public void handle(EnrollmentCreatedEvent event) {
        String message = "You're enrolled! Enrollment #" + event.enrollmentId()
                + " for course " + event.courseId();

        for (NotificationSender sender : senders) {
            sender.send(event.email(), message);
            repository.save(new Notification(
                    event.enrollmentId(), event.email(), sender.channel(), message, Instant.now()));
        }
    }
}