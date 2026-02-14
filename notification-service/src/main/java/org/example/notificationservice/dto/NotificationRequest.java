package org.example.notificationservice.dto;

public record NotificationRequest (
        Long userId,
        String type,     // MESSAGE, STATUS, etc.
        String message   // text
){}
