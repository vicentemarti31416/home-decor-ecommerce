package com.vcn.users.model;

import jakarta.validation.constraints.NotBlank;

public record ResetPassword(
        @NotBlank String code,
        @NotBlank String password
) {
}
