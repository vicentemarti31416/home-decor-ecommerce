package com.vcn.mail.model;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record OrderResponse(
        String id,
        Double amount,
        String status,
        Instant createdDate,
        List<ProductResponse> products
) {
}
