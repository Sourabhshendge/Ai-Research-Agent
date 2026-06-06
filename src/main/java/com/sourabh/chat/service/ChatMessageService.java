package com.sourabh.chat.service;

public interface ChatMessageService {

    void saveUserMessage(
            Long sessionId,
            String content
    );

    void saveAssistantMessage(
            Long sessionId,
            String content
    );
}
