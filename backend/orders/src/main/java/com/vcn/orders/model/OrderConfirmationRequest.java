package com.vcn.orders.model;

import lombok.Builder;

@Builder
public record OrderConfirmationRequest(
        String recipient,
        String subject,
        OrderResponse orderResponse
) {
}
