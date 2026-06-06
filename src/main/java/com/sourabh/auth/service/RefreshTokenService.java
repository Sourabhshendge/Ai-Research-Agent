package com.sourabh.auth.service;


import com.sourabh.auth.entity.RefreshToken;
import com.sourabh.auth.repository.RefreshTokenRepository;
import com.sourabh.security.jwt.JwtProperties;
import com.sourabh.user.domain.entity.User;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtProperties jwtProperties;

    public RefreshToken createRefreshToken(
            String token,
            User user
    ) {

        RefreshToken refreshToken =
                RefreshToken.builder()
                        .token(token)
                        .user(user)
                        .expiryDate(
                                LocalDateTime.now()
                                        .plusSeconds(
                                                jwtProperties.refreshTokenExpiration() / 1000
                                        )
                        )
                        .revoked(false)
                        .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public RefreshToken verifyRefreshToken(
            String token
    ) {

        RefreshToken refreshToken =
                refreshTokenRepository.findByTokenWithUser(token)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Refresh token not found"
                                )
                        );

        if (refreshToken.isRevoked()) {

            throw new RuntimeException(
                    "Refresh token revoked"
            );
        }

        if (refreshToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException(
                    "Refresh token expired"
            );
        }

        return refreshToken;
    }

    public void revokeToken(String token) {

        RefreshToken refreshToken =
                verifyRefreshToken(token);

        refreshToken.setRevoked(true);

        refreshTokenRepository.save(refreshToken);
    }
}
