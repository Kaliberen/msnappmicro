package org.example.messageservice.repository;

import org.example.messageservice.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);
    List<Message> findBySenderIdOrderByCreatedAtDesc(Long senderId);
}
