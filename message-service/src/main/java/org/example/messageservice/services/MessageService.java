package org.example.messageservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.messageservice.model.Message;
import org.example.messageservice.repository.MessageRepository;
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

    @Value("${user-service.base-url}")
    private  String userServiceBaseUrl;

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

    // Sends a message between two users
    public Message sendMessage(Message message) {

        // Validate sender and receiver before saving
        validateUserExists(message.getSenderId());
        validateUserExists(message.getReceiverId());

        log.info("Saving message senderId={} receiverId={}",
                message.getSenderId(),
                message.getReceiverId());

        return messageRepository.save(message);
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
