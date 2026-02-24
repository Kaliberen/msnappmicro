package org.example.messageservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.messageservice.dto.MessageRequest;
import org.example.messageservice.dto.MessageResponse;
import org.example.messageservice.mapper.MessageMapper;
import org.example.messageservice.services.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public MessageResponse send(@RequestBody MessageRequest request){
        log.debug("POST /messages senderId={} receiverId={}", request.senderId(), request.receiverId());
        return MessageMapper.toResponse(
                messageService.sendMessage(MessageMapper.toEntity(request))
        );
    }

    @GetMapping("/sent/{senderId}")
    public List<MessageResponse> sent(@PathVariable Long senderId){
        return messageService.getMessagesBySender(senderId)
                .stream()
                .map(MessageMapper::toResponse)
                .toList();
    }

    @GetMapping("/inbox/{receiverId}")
    public List<MessageResponse> inbox(@PathVariable Long receiverId){
        return messageService.getMessagesForReceiver(receiverId)
                .stream()
                .map(MessageMapper::toResponse)
                .toList();
    }
}
