package com.ng.runz.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Please fill in your username.")
        String username,
        @NotBlank(message = "Please fill in your password.")
        String password
) {
}
