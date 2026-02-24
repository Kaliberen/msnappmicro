package org.example.notificationservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.notificationservice.dto.NotificationRequest;
import org.example.notificationservice.model.Notification;
import org.example.notificationservice.services.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public Notification createNotification(@RequestBody NotificationRequest request) {
        return notificationService.create(
                request.userId(),
                request.type(),
                request.message()
        );
    }

    @GetMapping("/{userId}")
    public List<Notification> getByUserId(@PathVariable Long userId){

        return notificationService.getByUserId(userId);
    }
}
