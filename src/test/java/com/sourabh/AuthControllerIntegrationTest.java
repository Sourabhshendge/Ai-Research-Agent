package com.sourabh;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourabh.auth.dto.LoginRequest;
import com.sourabh.auth.dto.RefreshTokenRequest;
import com.sourabh.auth.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegisterLoginRefreshAndLogout() throws Exception {

        // ==========================
        // Register
        // ==========================

        RegisterRequest registerRequest = new RegisterRequest("Test User","test@example.com","Password@123");

        mockMvc.perform(
                        post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest))
                )
                .andExpect(status().isOk());

        // ==========================
        // Login
        // ==========================

        LoginRequest loginRequest = new LoginRequest("test@example.com","Password@123");


        String loginResponse =
                mockMvc.perform(
                                post("/api/v1/auth/login")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(loginRequest))
                        )
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        JsonNode loginJson =
                objectMapper.readTree(loginResponse);

        String accessToken =
                loginJson.path("data").path("accessToken").asText();

        String refreshToken =
                loginJson.path("data").path("refreshToken").asText();

        System.out.println(loginResponse);
        System.out.println("Refresh Token = " + refreshToken);

        assertThat(accessToken).isNotBlank();
        assertThat(refreshToken).isNotBlank();

        // ==========================
        // Refresh Token
        // ==========================

        RefreshTokenRequest refreshRequest =
                new RefreshTokenRequest(refreshToken);

        mockMvc.perform(
                        post("/api/v1/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(refreshRequest))
                )
                .andExpect(status().isOk());

        // ==========================
        // Logout
        // ==========================

        mockMvc.perform(
                        post("/api/v1/auth/logout")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(refreshRequest))
                )
                .andExpect(status().isOk());
    }
}