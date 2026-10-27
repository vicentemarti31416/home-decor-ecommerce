package com.vcn.chat.Service;

import com.vcn.chat.exception.BadRequestException;
import com.vcn.chat.exception.NotFoundException;
import com.vcn.chat.model.Chat;
import com.vcn.chat.model.ChatDTO;
import com.vcn.chat.model.ChatRoomType;
import com.vcn.chat.model.Participant;
import com.vcn.chat.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Service
public class ChatServiceImpl implements ChatService {

    @Value("${ADMIN}")
    private String admin;

    private final ParticipantService participantService;
    private final ChatRepository chatRepository;

    public ChatServiceImpl(ParticipantService participantService, ChatRepository chatRepository) {
        this.participantService = participantService;
        this.chatRepository = chatRepository;
    }

    @Override
    public ChatDTO getChat(String sender, String receiver) {
        if (receiver.equals("support") || !sender.equals(admin)) receiver = admin;
        if (sender.equals(receiver)) throw new BadRequestException("No es posible iniciar el chat");
        Function<String, Participant> participantSupplier = participantService::findByUsername;
        Participant senderDB = participantSupplier.apply(sender);
        Participant receiverDB = participantSupplier.apply(receiver);
        // Comprueba si existe ya un chat entre ambos participantes y, si existe, lo devuelve como ChatDTO
        Optional<Chat> optionalChat = chatRepository.findPrivateChat(sender, receiver);
        if (optionalChat.isPresent()) return toChatDTO(optionalChat.get());
        // Si no existe el chat, lo crea y lo guarda en la base de datos
        Chat chat = Chat.builder()
                .chatRoomType(ChatRoomType.PRIVATE)
                .participants(Set.of(senderDB, receiverDB))
                .build();
        Chat chatDB = save(chat);
        // Añade el chat creado a la lista de chats de cada usuario (solo el admin debe tener más de uno)
        senderDB.getChats().add(chatDB);
        receiverDB.getChats().add(chatDB);
        participantService.save(senderDB);
        participantService.save(receiverDB);

        return toChatDTO(chatDB);
    }

    @Override
    public Chat findById(Long id) {
        return chatRepository.findById(id).orElseThrow(() -> new NotFoundException(("Chat no encontrado")));
    }

    @Override
    public Chat save(Chat chat) {
        return chatRepository.save(chat);
    }

    @Override
    public ChatDTO toChatDTO(Chat chat) {
        return ChatDTO.builder()
                .id(chat.getId())
                .chatRoomType(chat.getChatRoomType().name())
                .participants(chat.getParticipants())
                .build();
    }
}
