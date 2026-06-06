package com.sourabh.user.domain.service;


import com.sourabh.common.exception.ResourceNotFoundException;
import com.sourabh.user.domain.entity.User;
import com.sourabh.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserServiceImpl implements CurrentUserService {

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        if (authentication == null) {
            throw new RuntimeException(
                    "No authenticated user found"
            );
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: " + email
                        ));
    }

    @Override
    public Long getCurrentUserId() {

        return getCurrentUser().getId();
    }

    @Override
    public String getCurrentUserEmail() {

        return getCurrentUser().getEmail();
    }
}