package org.example.messageservice.mapper;

import org.example.messageservice.dto.MessageRequest;
import org.example.messageservice.dto.MessageResponse;
import org.example.messageservice.model.Message;

public class MessageMapper {

    public static Message toEntity(MessageRequest req) {
        Message message = new Message();
        message.setSenderId(req.senderId());
        message.setReceiverId(req.receiverId());
        message.setContent(req.content());
        return message;
    }

    public static MessageResponse toResponse(Message m) {
        return new MessageResponse(
                m.getId(),
                m.getSenderId(),
                m.getReceiverId(),
                m.getContent(),
                m.getCreatedAt()
        );
    }
}
