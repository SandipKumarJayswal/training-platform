package com.example.notifications.core.notification;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "enrollment_id", nullable = false)
    private Long enrollmentId;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false)
    private String channel;

    @Column(nullable = false)
    private String message;

    @Column(name = "sent_at", nullable = false)
    private Instant sentAt;

    protected Notification() { }

    public Notification(Long enrollmentId, String recipient, String channel, String message, Instant sentAt) {
        this.enrollmentId = enrollmentId;
        this.recipient = recipient;
        this.channel = channel;
        this.message = message;
        this.sentAt = sentAt;
    }

    public Long getId() { return id; }
    public Long getEnrollmentId() { return enrollmentId; }
    public String getRecipient() { return recipient; }
    public String getChannel() { return channel; }
}