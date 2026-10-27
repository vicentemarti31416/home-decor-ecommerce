package com.vcn.users.model;

public record ResetPassword(
        String code,
        String password
) {
}
