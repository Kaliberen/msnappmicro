package org.example.notificationservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.example.notificationservice.model.Notification;
import org.example.notificationservice.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Notification create(Long userId, String type, String message) {
        Notification notification = Notification.builder()
                .userId(userId)
                .type(type)
                .message(message)
                .createdAt(Instant.now())
                .build();

        log.info("Saving notification userId={} type={}", userId, type);

        return notificationRepository.save(notification);
    }

    public List<Notification> getByUserId(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
