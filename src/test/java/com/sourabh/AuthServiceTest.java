package com.sourabh;

import com.sourabh.auth.dto.AuthResponse;
import com.sourabh.auth.dto.LoginRequest;
import com.sourabh.auth.dto.RefreshTokenRequest;
import com.sourabh.auth.dto.RegisterRequest;
import com.sourabh.auth.entity.RefreshToken;
import com.sourabh.auth.service.AuthService;
import com.sourabh.auth.service.RefreshTokenService;
import com.sourabh.security.jwt.JwtService;
import com.sourabh.security.service.CustomUserDetailsService;
import com.sourabh.user.domain.entity.Role;
import com.sourabh.user.domain.entity.User;
import com.sourabh.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthService authService;

    private User user;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .password("encoded-password")
                .role(Role.ROLE_USER)
                .build();

        userDetails =
                new org.springframework.security.core.userdetails.User(
                        "test@example.com",
                        "password",
                        java.util.List.of()
                );
    }

    // =========================================================
    // REGISTER
    // =========================================================

    @Test
    void shouldRegisterUserSuccessfully() {

        RegisterRequest request =
                new RegisterRequest(
                        "Test User",
                        "test@example.com",
                        "password"
                );

        when(userRepository.existsByEmail(
                request.email()
        )).thenReturn(false);

        when(passwordEncoder.encode(
                request.password()
        )).thenReturn("encoded-password");

        authService.register(request);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {

        RegisterRequest request =
                new RegisterRequest(
                        "Test User",
                        "test@example.com",
                        "password"
                );

        when(userRepository.existsByEmail(
                request.email()
        )).thenReturn(true);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> authService.register(request)
                );

        assertEquals(
                "Email already exists",
                exception.getMessage()
        );

        verify(userRepository, never())
                .save(any());
    }

    // =========================================================
    // LOGIN
    // =========================================================

    @Test
    void shouldLoginSuccessfully() {

        LoginRequest request =
                new LoginRequest(
                        "test@example.com",
                        "password"
                );

        when(userDetailsService.loadUserByUsername(
                request.email()
        )).thenReturn(userDetails);

        when(userRepository.findByEmail(
                request.email()
        )).thenReturn(Optional.of(user));

        when(jwtService.generateToken(
                userDetails
        )).thenReturn("access-token");

        when(jwtService.generateRefreshToken(
                userDetails
        )).thenReturn("refresh-token");

        AuthResponse response =
                authService.login(request);

        assertNotNull(response);

        assertEquals(
                "access-token",
                response.accessToken()
        );

        assertEquals(
                "refresh-token",
                response.refreshToken()
        );

        verify(refreshTokenService)
                .createRefreshToken(
                        "refresh-token",
                        user
                );
    }

    @Test
    void shouldThrowWhenUserNotFoundDuringLogin() {

        LoginRequest request =
                new LoginRequest(
                        "test@example.com",
                        "password"
                );

        when(userDetailsService.loadUserByUsername(
                request.email()
        )).thenReturn(userDetails);

        when(userRepository.findByEmail(
                request.email()
        )).thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> authService.login(request)
                );

        assertEquals(
                "User not found",
                exception.getMessage()
        );
    }

    // =========================================================
    // REFRESH TOKEN
    // =========================================================

    @Test
    void shouldRefreshAccessToken() {

        RefreshTokenRequest request =
                new RefreshTokenRequest();

        request.setRefreshToken("refresh-token");

        RefreshToken refreshToken =
                RefreshToken.builder()
                        .token("refresh-token")
                        .user(user)
                        .expiryDate(
                                LocalDateTime.now().plusDays(1)
                        )
                        .revoked(false)
                        .build();

        when(refreshTokenService.verifyRefreshToken(
                "refresh-token"
        )).thenReturn(refreshToken);

        when(userDetailsService.loadUserByUsername(
                user.getEmail()
        )).thenReturn(userDetails);

        when(jwtService.generateToken(
                userDetails
        )).thenReturn("new-access-token");

        AuthResponse response =
                authService.refreshToken(request);

        assertEquals(
                "new-access-token",
                response.accessToken()
        );

        assertEquals(
                "refresh-token",
                response.refreshToken()
        );
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    @Test
    void shouldLogoutSuccessfully() {

        RefreshTokenRequest request =
                new RefreshTokenRequest();

        request.setRefreshToken("refresh-token");

        authService.logout(request);

        verify(refreshTokenService)
                .revokeToken("refresh-token");
    }
}

