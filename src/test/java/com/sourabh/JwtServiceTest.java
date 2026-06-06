package com.sourabh;

import com.sourabh.security.jwt.JwtProperties;
import com.sourabh.security.jwt.JwtService;
import com.sourabh.user.domain.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    private JwtService jwtService;

    private UserDetails userDetails;

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {

        JwtProperties jwtProperties =
                new JwtProperties(
                        "mysupersecretkeymysupersecretkeymysupersecretkey123456",
                        86400000L,
                        604800000L
                );

        jwtService = new JwtService(jwtProperties);


        userDetails =
                new org.springframework.security.core.userdetails.User(
                        "test@example.com",
                        "password",
                        List.of(
                                new SimpleGrantedAuthority("ROLE_USER")
                        )
                );
    }

    @Test
    void shouldGenerateAccessToken() {

        String token =
                jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void shouldExtractUsernameFromToken() {

        String token =
                jwtService.generateToken(userDetails);

        String username =
                jwtService.extractUsername(token);

        assertEquals(
                "test@example.com",
                username
        );
    }

    @Test
    void shouldValidateToken() {

        String token =
                jwtService.generateToken(userDetails);

        boolean valid =
                jwtService.isTokenValid(
                        token,
                        userDetails
                );

        assertTrue(valid);
    }

    @Test
    void shouldReturnFalseForDifferentUser() {

        String token =
                jwtService.generateToken(userDetails);


        UserDetails anotherUser =
                new org.springframework.security.core.userdetails.User(
                        "other@example.com",
                        "password",
                        List.of(
                                new SimpleGrantedAuthority("ROLE_USER")
                        )
                );

        boolean valid =
                jwtService.isTokenValid(
                        token,
                        anotherUser
                );

        assertFalse(valid);
    }

    @Test
    void shouldGenerateRefreshToken() {

        String token =
                jwtService.generateRefreshToken(
                        userDetails
                );

        assertNotNull(token);
    }

    @Test
    void shouldContainRefreshTypeClaim() {

        String token =
                jwtService.generateRefreshToken(
                        userDetails
                );

        String type =
                jwtService.extractClaim(
                        token,
                        claims -> claims.get(
                                "type",
                                String.class
                        )
                );

        assertEquals(
                "REFRESH",
                type
        );
    }

    @Test
    void accessTokenShouldNotContainRefreshClaim() {

        String token =
                jwtService.generateToken(userDetails);

        String type =
                jwtService.extractClaim(
                        token,
                        claims -> claims.get(
                                "type",
                                String.class
                        )
                );

        assertNull(type);
    }

    @Test
    void shouldExtractExpirationDate() {

        String token =
                jwtService.generateToken(userDetails);

        Date expiration =
                jwtService.extractClaim(
                        token,
                        Claims::getExpiration
                );

        assertNotNull(expiration);
    }

    @Test
    void shouldStoreCustomClaims() {

        HashMap<String,Object> claims =
                new HashMap<>();

        claims.put(
                "role",
                "ADMIN"
        );

        String token =
                jwtService.generateToken(
                        claims,
                        userDetails
                );

        String role =
                jwtService.extractClaim(
                        token,
                        c -> c.get(
                                "role",
                                String.class
                        )
                );

        assertEquals(
                "ADMIN",
                role
        );
    }
}
