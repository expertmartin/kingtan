package com.kingtan.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

public record UserDTO(
        Long id,

        @NotBlank(message = "Username cannot be blank")
        String username,

        @Email(message = "Email must be valid")
        @NotBlank(message = "Email cannot be blank")
        String email,

        Set<String> roles
) {}