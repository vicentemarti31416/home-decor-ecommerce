package com.vcn.users.model;

import java.util.List;
import java.util.Set;

public record RegisterRequest(
        String username,
        String password,
        String name,
        String lastName,
        String address,
        String city,
        String postalCode,
        String email,
        String phone,
        Set<Long> favoriteIds,
        List<Role> roles
) {
    public List<Role> roles() {
        return (roles == null || roles.isEmpty())
                ? List.of(Role.ROLE_USER)
                : roles;
    }
}
