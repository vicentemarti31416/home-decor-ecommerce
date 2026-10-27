package com.vcn.orders.model;

public record StatusRequest(
        String orderId,
        String status
) {
}
