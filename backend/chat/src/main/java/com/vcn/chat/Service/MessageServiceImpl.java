package com.vcn.chat.Service;

import com.vcn.chat.model.*;
import com.vcn.chat.model.Message;
import com.vcn.chat.model.MessageDTO;
import com.vcn.chat.repository.ChatRepository;
import com.vcn.chat.repository.MessageRepository;
import com.vcn.chat.repository.ParticipantRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements com.vcn.chat.Service.MessageService {

    private final MessageRepository messageRepository;
    private final ParticipantService participantService;
    private final ChatService chatService;

    public MessageServiceImpl(MessageRepository messageRepository, ParticipantService participantService, ChatService chatService) {
        this.messageRepository = messageRepository;
        this.participantService = participantService;
        this.chatService = chatService;
    }

    @Override
    public List<MessageDTO> findAllById(Long chatId) {
        return listMessageToListMessageDTO(messageRepository.findByChatIdOrderByCreatedAsc(chatId));
    }

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Message messageDTOToMessage(MessageDTO messageDTO) {
        Participant senderDB = participantService.findByUsername(messageDTO.sender());
        Chat chatDB = chatService.findById(messageDTO.chatId());
        return Message.builder()
                .id(null)
                .sender(senderDB)
                .message(messageDTO.message())
                .chat(chatDB)
                .created(null)
                .messageType(MessageType.valueOf(messageDTO.messageType()))
                .build();
    }

    @Override
    public List<MessageDTO> listMessageToListMessageDTO(List<Message> messages) {
        return messages.stream().map(this::messageToMessageDTO).toList();
    }

    @Override
    public MessageDTO messageToMessageDTO(Message message) {
        return new MessageDTO(
                message.getId(),
                message.getSender().getUsername(),
                message.getMessage(),
                message.getChat().getId(),
                message.getMessageType().toString(),
                message.getCreated()
        );
    }
}
