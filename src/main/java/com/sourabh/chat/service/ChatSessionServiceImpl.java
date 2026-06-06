package com.sourabh.chat.service;

import com.sourabh.chat.dto.CreateSessionRequest;
import com.sourabh.chat.dto.MessageResponse;
import com.sourabh.chat.dto.SessionResponse;
import com.sourabh.chat.entity.ChatSession;
import com.sourabh.chat.repository.ChatMessageRepository;
import com.sourabh.chat.repository.ChatSessionRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatSessionServiceImpl implements ChatSessionService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public SessionResponse createSession(
            Long userId,
            CreateSessionRequest request) {

        ChatSession session = ChatSession.builder()
                .title(request.getTitle())
                .userId(userId)
                .build();

        ChatSession savedSession =
                chatSessionRepository.save(session);

        return SessionResponse.builder()
                .id(savedSession.getId())
                .title(savedSession.getTitle())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionResponse> getSessions(Long userId) {

        return chatSessionRepository
                .findByUserIdOrderByUpdatedAtDesc(userId)
                .stream()
                .map(session -> SessionResponse.builder()
                        .id(session.getId())
                        .title(session.getTitle())
                        .build())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageResponse> getMessages(
            Long userId,
            Long sessionId) {

        ChatSession session =
                validateOwnership(userId, sessionId);

        return chatMessageRepository
                .findBySessionIdOrderByCreatedAtAsc(
                        session.getId()
                )
                .stream()
                .map(message -> MessageResponse.builder()
                        .role(message.getRole())
                        .content(message.getContent())
                        .build())
                .toList();
    }

    private ChatSession validateOwnership(
            Long userId,
            Long sessionId) {

        ChatSession session =
                chatSessionRepository
                        .findById(sessionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Session not found"
                                ));

        if (!session.getUserId().equals(userId)) {
            throw new RuntimeException(
                    "Access denied"
            );
        }

        return session;
    }
}
