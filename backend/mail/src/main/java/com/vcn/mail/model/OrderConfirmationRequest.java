package com.vcn.mail.model;

import lombok.Builder;

@Builder
public record OrderConfirmationRequest(
        String recipient,
        String subject,
        OrderResponse orderResponse
) {
}
