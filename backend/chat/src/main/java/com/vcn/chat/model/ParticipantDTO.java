package com.vcn.chat.model;

import lombok.Builder;

@Builder
public record ParticipantDTO(
        String id,
        String username,
        String photo
) { }
