package org.example.messageservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.messageservice.config.RabbitConfig;
import org.example.messageservice.dto.MessageSentEvent;
import org.example.messageservice.dto.NotificationRequest;
import org.example.messageservice.model.Message;
import org.example.messageservice.repository.MessageRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;

    // RestClient replacement for RestTemplate
    private final RestClient restClient = RestClient.create();

    private final RabbitTemplate rabbitTemplate;

    @Value("${user-service.base-url}")
    private  String userServiceBaseUrl;

    @Value("${notification-service.base-url}")
    private  String notificationServiceBaseUrl;


    // Validate if a user exist by calling user-service
    private void validateUserExists(Long userId) {
        try {
            restClient.get()
                    .uri(userServiceBaseUrl + "/users/{id}", userId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.warn("User not found in user-service: id={}", userId);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "User does not exist " + userId);
        }
    }

    private void createNotificationForReceiver(Message savedMessage) {

        // check for value
        log.info("Notification-service.base-url={}", notificationServiceBaseUrl);

        try {
            restClient.post()
                    .uri(notificationServiceBaseUrl + "/notifications")
                    .body(new NotificationRequest(
                            savedMessage.getReceiverId(),
                            "MESSAGE",
                            "New message from user " +  savedMessage.getSenderId()
                    ))
                    .retrieve()
                    .toBodilessEntity();
            log.debug("Notification created for receiverId={}", savedMessage.getReceiverId());
        }  catch (Exception e) {
            log.warn("Could not reach notification-service. Message saved. receiverId={}",
                    savedMessage.getReceiverId(), e);
        }
    }


    // Sends a message between two users
    public Message sendMessage(Message message) {

        // Validate sender and receiver before saving
        validateUserExists(message.getSenderId());
        validateUserExists(message.getReceiverId());

        log.info("Saving message senderId={} receiverId={}",
                message.getSenderId(),
                message.getReceiverId());

        // Save message in database
        Message savedMessage = messageRepository.save(message);

        try {
            log.info("Publishing MessageSentEvent messageId={} receiverId={}",
                    savedMessage.getId(),
                    message.getReceiverId());


            rabbitTemplate.convertAndSend(
                    RabbitConfig.EXCHANGE_NAME,
                    RabbitConfig.ROUTING_KEY_MESSAGE_SENT,
                    new MessageSentEvent(
                            savedMessage.getId(),
                            savedMessage.getSenderId(),
                            savedMessage.getReceiverId(),
                            savedMessage.getContent(),
                            savedMessage.getCreatedAt()
                    )
            );
        } catch (Exception e) {

            log.warn("Failed to publish event to RabbitMQ. Message is saved anyway. MessageId=[}",
                    savedMessage.getId(), e);
        }

        return savedMessage;

        //createNotificationForReceiver(savedMessage);
        //return savedMessage;
    }

    // Get all messages for given receiver.
    public List<Message> getMessagesForReceiver(Long receiverId) {
        return messageRepository.findByReceiverIdOrderByCreatedAtDesc(receiverId);
    }

    // Get all messages sent by given sender
    public List<Message> getMessagesBySender(Long senderId) {
        return messageRepository.findBySenderIdOrderByCreatedAtDesc(senderId);
    }

}
