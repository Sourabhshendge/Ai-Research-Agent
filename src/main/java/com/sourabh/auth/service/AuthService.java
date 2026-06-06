package com.sourabh.auth.service;
import com.sourabh.auth.dto.AuthResponse;
import com.sourabh.auth.dto.LoginRequest;
import com.sourabh.auth.dto.RefreshTokenRequest;
import com.sourabh.auth.dto.RegisterRequest;
import com.sourabh.auth.entity.RefreshToken;
import com.sourabh.security.jwt.JwtService;
import com.sourabh.security.service.CustomUserDetailsService;
import com.sourabh.user.domain.entity.Role;
import com.sourabh.user.domain.entity.User;
import com.sourabh.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsService userDetailsService;

    private final RefreshTokenService refreshTokenService;

    // =========================================================
    // REGISTER
    // =========================================================

    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {

            throw new RuntimeException(
                    "Email already exists"
            );
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(
                        passwordEncoder.encode(
                                request.password()
                        )
                )
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
    }

    // =========================================================
    // LOGIN
    // =========================================================

    public AuthResponse login(
            LoginRequest request
    ) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        request.email()
                );

        User user = userRepository.findByEmail(
                request.email()
        ).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        String accessToken =
                jwtService.generateToken(userDetails);

        String refreshToken =
                jwtService.generateRefreshToken(userDetails);

        refreshTokenService.createRefreshToken(
                refreshToken,
                user
        );

        return new AuthResponse(
                accessToken,
                refreshToken
        );
    }

    // =========================================================
    // REFRESH TOKEN
    // =========================================================

    public AuthResponse refreshToken(
            RefreshTokenRequest request
    ) {

        RefreshToken refreshToken =
                refreshTokenService.verifyRefreshToken(
                        request.getRefreshToken()
                );

        User user = refreshToken.getUser();

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        user.getEmail()
                );

        String newAccessToken =
                jwtService.generateToken(userDetails);

        return new AuthResponse(
                newAccessToken,
                request.getRefreshToken()
        );
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    public void logout(
            RefreshTokenRequest request
    ) {

        refreshTokenService.revokeToken(
                request.getRefreshToken()
        );
    }
}