package org.example.messageservice.dto;

public record MessageRequest(
        Long senderId,
        Long receiverId,
        String content
) {
}
