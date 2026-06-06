package com.sourabh;

import com.sourabh.auth.entity.RefreshToken;
import com.sourabh.auth.repository.RefreshTokenRepository;
import com.sourabh.auth.service.RefreshTokenService;
import com.sourabh.user.domain.entity.Role;
import com.sourabh.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    private User user;

    @BeforeEach
    void setUp() {

        ReflectionTestUtils.setField(
                refreshTokenService,
                "refreshTokenExpiration",
                604800000L
        );

        user = User.builder()
                .email("test@example.com")
                .password("password")
                .role(Role.ROLE_USER)
                .build();
    }

    @Test
    void shouldCreateRefreshToken() {

        RefreshToken refreshToken =
                RefreshToken.builder()
                        .token("refresh-token")
                        .user(user)
                        .expiryDate(LocalDateTime.now().plusDays(7))
                        .revoked(false)
                        .build();

        when(refreshTokenRepository.save(any()))
                .thenReturn(refreshToken);

        RefreshToken result =
                refreshTokenService.createRefreshToken(
                        "refresh-token",
                        user
                );

        assertNotNull(result);
        assertEquals(
                "refresh-token",
                result.getToken()
        );

        verify(refreshTokenRepository)
                .save(any());
    }

    @Test
    void shouldVerifyValidRefreshToken() {

        RefreshToken refreshToken =
                RefreshToken.builder()
                        .token("token")
                        .user(user)
                        .expiryDate(LocalDateTime.now().plusDays(1))
                        .revoked(false)
                        .build();

        when(refreshTokenRepository.findByTokenWithUser("token"))
                .thenReturn(Optional.of(refreshToken));

        RefreshToken result =
                refreshTokenService.verifyRefreshToken(
                        "token"
                );

        assertNotNull(result);

        verify(refreshTokenRepository)
                .findByTokenWithUser("token");
    }

    @Test
    void shouldThrowWhenTokenNotFound() {

        when(refreshTokenRepository.findByTokenWithUser("token"))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> refreshTokenService
                                .verifyRefreshToken("token")
                );

        assertEquals(
                "Refresh token not found",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowWhenTokenRevoked() {

        RefreshToken refreshToken =
                RefreshToken.builder()
                        .token("token")
                        .user(user)
                        .expiryDate(LocalDateTime.now().plusDays(1))
                        .revoked(true)
                        .build();

        when(refreshTokenRepository.findByTokenWithUser("token"))
                .thenReturn(Optional.of(refreshToken));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> refreshTokenService
                                .verifyRefreshToken("token")
                );

        assertEquals(
                "Refresh token revoked",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowWhenTokenExpired() {

        RefreshToken refreshToken =
                RefreshToken.builder()
                        .token("token")
                        .user(user)
                        .expiryDate(LocalDateTime.now().minusMinutes(1))
                        .revoked(false)
                        .build();

        when(refreshTokenRepository.findByTokenWithUser("token"))
                .thenReturn(Optional.of(refreshToken));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> refreshTokenService
                                .verifyRefreshToken("token")
                );

        assertEquals(
                "Refresh token expired",
                exception.getMessage()
        );
    }

    @Test
    void shouldRevokeToken() {

        RefreshToken refreshToken =
                RefreshToken.builder()
                        .token("token")
                        .user(user)
                        .expiryDate(LocalDateTime.now().plusDays(1))
                        .revoked(false)
                        .build();

        when(refreshTokenRepository.findByTokenWithUser("token"))
                .thenReturn(Optional.of(refreshToken));

        when(refreshTokenRepository.save(any()))
                .thenReturn(refreshToken);

        refreshTokenService.revokeToken("token");

        assertTrue(refreshToken.isRevoked());

        verify(refreshTokenRepository)
                .save(refreshToken);
    }
}
