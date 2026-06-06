package com.sourabh.chat.service;

import com.sourabh.chat.dto.*;

import java.util.List;

public interface ChatSessionService {

    SessionResponse createSession(
            Long userId,
            CreateSessionRequest request
    );

    List<SessionResponse> getSessions(
            Long userId
    );

    List<MessageResponse> getMessages(
            Long userId,
            Long sessionId
    );
}