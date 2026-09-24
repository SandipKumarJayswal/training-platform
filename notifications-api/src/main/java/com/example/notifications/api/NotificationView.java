package com.example.notifications.api;

import java.time.Instant;

public record NotificationView(
        Long id,
        Long enrollmentId,
        String recipient,
        String channel,
        String message,
        Instant sentAt
) {
}