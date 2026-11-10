package com.vcn.users.model;

import lombok.Builder;

@Builder
public record EmailRequest(
        String recipient,
        String subject,
        String message
) {
}
