package com.ng.runz.request;

import com.ng.runz.dto.RoleDto;
import com.ng.runz.model.Role;
import jakarta.persistence.Embedded;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record RegisterUserRequest(
        @NotBlank
        String username,
        @NotBlank
        String password,
        @NotBlank
        String email,
        @NotEmpty
        List<String> roles
) {
}
