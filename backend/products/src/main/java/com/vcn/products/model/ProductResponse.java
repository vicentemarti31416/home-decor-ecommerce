package com.vcn.products.model;

import lombok.Builder;

@Builder
public record ProductResponse(
        Long id,
        String name,
        Double price,
        String reference,
        String photo,
        Double rating
) { }
