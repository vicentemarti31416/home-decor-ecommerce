package com.vcn.products.model;

import lombok.Builder;

@Builder
public record ReviewResponse(
        Long id,
        Integer stars,
        String comment,
        String customerName
) {
}
