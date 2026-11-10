package com.vcn.payments.model;

import java.util.Map;

public record IntentRequest(
    Long amount,
    String currency,
    Map<String, String> metadata
) {
}
