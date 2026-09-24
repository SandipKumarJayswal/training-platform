package com.example.notifications.core.notification;

public interface NotificationSender {
    String channel();
    void send(String recipient, String message);
}