package com.vcn.products.model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductRecord(
        Long id,
        BigDecimal price,
        Long stock
) {
}
