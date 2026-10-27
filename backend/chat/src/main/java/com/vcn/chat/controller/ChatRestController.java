package com.vcn.chat.controller;

import com.vcn.chat.Service.ChatService;
import com.vcn.chat.Service.MessageService;
import com.vcn.chat.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatRestController {

    private final ChatService chatService;
    private final MessageService messageService;

    public ChatRestController(ChatService chatService, MessageService messageService) {
        this.chatService = chatService;
        this.messageService = messageService;
    }

    // TODO: Falta hacer que cuando un usuario se registra se cree un Participant con sus datos con RestClient
    @GetMapping("/chat/{receiver}")
    public ResponseEntity<?> getChat(@AuthenticationPrincipal Jwt jwt, @PathVariable String receiver) {
        return ResponseEntity.ok(chatService.getChat(jwt.getSubject(), receiver));
    }

    /**
     * Esta función devuelve todos los mensajes de un chat mediante su id y el username.
     * Por motivos de seguridad solo ha de devolver los chats en los que el username coincida
     */
    @GetMapping("/messages/{chatId}")
    public ResponseEntity<?> getMessages(@PathVariable Long chatId, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(messageService.findAllById(chatId));
    }
}
