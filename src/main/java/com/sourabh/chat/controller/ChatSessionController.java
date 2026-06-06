package com.sourabh.chat.controller;

import com.sourabh.chat.dto.CreateSessionRequest;
import com.sourabh.chat.dto.MessageResponse;
import com.sourabh.chat.dto.SessionResponse;
import com.sourabh.chat.service.ChatSessionService;
import com.sourabh.common.response.ApiResponse;
import com.sourabh.user.domain.service.CurrentUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat/sessions")
@RequiredArgsConstructor
public class ChatSessionController {

    private final ChatSessionService service;
    private final CurrentUserService currentUserService;

    @PostMapping
    public ApiResponse<SessionResponse> create(
            @Valid
            @RequestBody CreateSessionRequest request
    ) {

        Long userId =currentUserService.getCurrentUserId();

        return ApiResponse.success(
                service.createSession(
                        userId,
                        request
                )
        );
    }

    @GetMapping
    public ApiResponse<List<SessionResponse>> getSessions() {

        Long userId = currentUserService.getCurrentUserId();

        return ApiResponse.success(
                service.getSessions(userId)
        );
    }

    @GetMapping("/{sessionId}/messages")
    public ApiResponse<List<MessageResponse>> getMessages(
            @PathVariable Long sessionId
    ) {

        Long userId = currentUserService.getCurrentUserId();

        return ApiResponse.success(
                service.getMessages(
                        userId,
                        sessionId
                )
        );
    }

}
