package com.sourabh.auth.controller;

import com.sourabh.auth.dto.AuthResponse;
import com.sourabh.auth.dto.LoginRequest;
import com.sourabh.auth.dto.RefreshTokenRequest;
import com.sourabh.auth.dto.RegisterRequest;
import com.sourabh.auth.service.AuthService;
import com.sourabh.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // =========================================================
    // REGISTER
    // =========================================================
    @Operation(
            summary = "Register new user",
            description = "Creates a new user account"
    )
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        authService.register(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "User registered successfully",
                        null
                )
        );
    }

    // =========================================================
    // LOGIN
    // =========================================================
    @Operation(
            summary = "Authenticate user",
            description = "Returns access and refresh token"
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {

        AuthResponse response =
                authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Login successful",
                        response
                )
        );
    }

    // =========================================================
    // REFRESH TOKEN
    // =========================================================

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>>
    refreshToken(
            @Valid @RequestBody
            RefreshTokenRequest request
    ) {

        AuthResponse response =
                authService.refreshToken(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Access token refreshed successfully",
                        response
                )
        );
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>>
    logout(
            @Valid @RequestBody
            RefreshTokenRequest request
    ) {

        authService.logout(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Logout successful",
                        null
                )
        );
    }
}