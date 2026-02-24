package org.example.messageservice.dto;

public record NotificationRequest(
        Long userId,
        String type,
        String message
) {
}
