package com.vcn.products.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RatingRequest(
        @Min(1) @Max(5) Integer stars,
        String comment,
        @NotNull Long productId
) {
}
