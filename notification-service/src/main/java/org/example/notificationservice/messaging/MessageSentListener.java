package org.example.notificationservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.config.RabbitConfig;
import org.example.notificationservice.dto.MessageSentEvent;
import org.example.notificationservice.services.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageSentListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitConfig.QUEUE_NOTIFICATIONS)
    public void onMessage(MessageSentEvent event) {
        log.info("Received MessageSentEvent: messageId={}, senderId={}, receiverId={}",
                event.messageId(), event.senderId(), event.receiverId());

        notificationService.create(
                event.receiverId(),
                "MESSAGE",
                "New message from user " + event.senderId()
        );
    }

}
