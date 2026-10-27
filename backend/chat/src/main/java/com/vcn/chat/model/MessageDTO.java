package com.vcn.chat.model;

import java.time.Instant;

public record MessageDTO(
        Long id,
        String sender,
        String message,
        Long chatId,
        String messageType,
        Instant created
) { }
