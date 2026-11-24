package com.vcn.users.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record RegisterRequest(
        @NotBlank @Email String username,
        @NotBlank String password,
        String name,
        String lastName,
        List<Role> roles
) {
    public List<Role> roles() {
        return (roles == null || roles.isEmpty())
                ? List.of(Role.ROLE_USER)
                : roles;
    }
}
