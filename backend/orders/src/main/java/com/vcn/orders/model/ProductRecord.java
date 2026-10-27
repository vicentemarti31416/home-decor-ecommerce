package com.vcn.orders.model;

import java.math.BigDecimal;

public record ProductRecord(
        Long id,
        BigDecimal price,
        Long stock
) {
}
