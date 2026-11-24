package com.vcn.users.model;

import jakarta.validation.constraints.NotBlank;

public record ShipmentDetailsRequest(
        @NotBlank String address,
        @NotBlank String city,
        @NotBlank String postalCode,
        String phone
) {}
