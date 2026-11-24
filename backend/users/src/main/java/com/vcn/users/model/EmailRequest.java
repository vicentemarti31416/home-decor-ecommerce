package com.vcn.users.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record EmailRequest(
        @Email @NotBlank String recipient,
        String subject,
        @NotBlank String message
) {
}
