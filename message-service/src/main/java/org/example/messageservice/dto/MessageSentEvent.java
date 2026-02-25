package org.example.messageservice.dto;

import java.time.Instant;

public record MessageSentEvent(
        Long messageId,
        Long senderId,
        Long receiverId,
        String content,
        Instant createdAt
) {
}
