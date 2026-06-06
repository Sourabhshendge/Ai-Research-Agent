package com.sourabh.auth.controller;

// ============================================================
// TASK 7 — Secure Test Controller
// package: com.sourabh.test
// ============================================================


import com.sourabh.common.response.ApiResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping("/secure")
    public ApiResponse<String> secureEndpoint() {

        return ApiResponse.success(
                "Secure API Accessed Successfully"
        );
    }

    @GetMapping("/me")
    public Authentication me(Authentication authentication) {
        return authentication;
    }
}
