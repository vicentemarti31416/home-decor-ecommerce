package com.vcn.chat.Service;

import com.vcn.chat.model.Message;
import com.vcn.chat.model.MessageDTO;

import java.util.List;

public interface MessageService {

    List<MessageDTO> findAllById(Long chatId);

    Message save(Message message);

    Message messageDTOToMessage(MessageDTO messageDTO);

    List<MessageDTO> listMessageToListMessageDTO(List<Message> messages);

    MessageDTO messageToMessageDTO(Message message);
}
