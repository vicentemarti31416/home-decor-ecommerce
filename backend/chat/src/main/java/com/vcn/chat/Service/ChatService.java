package com.vcn.chat.Service;

import com.vcn.chat.model.Chat;
import com.vcn.chat.model.ChatDTO;

public interface ChatService {

    ChatDTO getChat(String sender, String receiver);
    Chat findById(Long id);
    ChatDTO toChatDTO(Chat chat);
}
