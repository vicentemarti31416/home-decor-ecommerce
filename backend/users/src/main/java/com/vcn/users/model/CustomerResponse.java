package com.vcn.users.model;

import lombok.Builder;

import java.util.List;
import java.util.Set;

@Builder
public record CustomerResponse(
        String name,
        String lastName,
        String username,
        List<String> roles,
        String address,
        String city,
        String postalCode,
        String phone,
        String photo,
        Set<Long> favoriteIds
) {
}
