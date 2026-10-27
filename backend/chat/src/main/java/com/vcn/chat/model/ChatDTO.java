package com.vcn.chat.model;

import lombok.Builder;

import java.util.Set;

@Builder
public record ChatDTO(
        Long id,
        String chatRoomType,
        Set<Participant> participants
) { }
