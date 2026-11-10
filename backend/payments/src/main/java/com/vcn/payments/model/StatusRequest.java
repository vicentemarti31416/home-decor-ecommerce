package com.vcn.payments.model;

public record StatusRequest(
        String orderId,
        String status
) {
}
