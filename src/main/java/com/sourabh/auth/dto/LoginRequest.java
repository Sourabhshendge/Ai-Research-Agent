package com.sourabh.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @Email(message = "Invalid email")
        String email,

        @NotBlank(message = "Password is required")
        String password
) {
}
