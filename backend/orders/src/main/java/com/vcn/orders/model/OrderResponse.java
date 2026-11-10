package com.vcn.orders.model;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record OrderResponse(
        String id,
        Double amount,
        String status,
        Instant createdDate,
        Instant updatedDate,
        List<ProductResponse> products
) {
}
