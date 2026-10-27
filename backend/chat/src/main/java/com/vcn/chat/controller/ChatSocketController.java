package com.vcn.chat.controller;


import com.vcn.chat.Service.MessageService;
import com.vcn.chat.model.MessageDTO;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatSocketController {

    private final MessageService messageService;

    public ChatSocketController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/chat/{chatId}")
    @SendTo("/chat/{chatId}")
    public MessageDTO publicMessage(@DestinationVariable String chatId, @Payload MessageDTO messageDTO){
        return messageService.messageToMessageDTO(messageService.save(messageService.messageDTOToMessage(messageDTO)));
    }
}
