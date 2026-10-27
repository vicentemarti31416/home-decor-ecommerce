package com.vcn.orders.model;

import java.util.Map;

public record IntentRequest(
        Long amount,
        String currency,
        Map<String, String> metadata
) {
}
