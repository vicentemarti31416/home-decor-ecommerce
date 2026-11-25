package com.vcn.products.model;

import java.util.Map;

public record UpdateStockEvent(
        String orderId,
        Map<Long, Long> products)
{}
