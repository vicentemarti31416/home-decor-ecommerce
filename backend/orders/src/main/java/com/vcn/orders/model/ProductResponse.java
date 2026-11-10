package com.vcn.orders.model;

import lombok.Builder;

@Builder
public record ProductResponse(
        Long id,
        String name,
        Double price,
        Long quantity,
        String reference,
        String photo
) { }
