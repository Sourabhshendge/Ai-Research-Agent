package com.sourabh.chat.service;

import com.sourabh.chat.entity.ChatMessage;
import com.sourabh.chat.entity.MessageRole;
import com.sourabh.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    @Override
    public void saveUserMessage(
            Long sessionId,
            String content) {

        ChatMessage message = ChatMessage.builder()
                .sessionId(sessionId)
                .role(MessageRole.USER)
                .content(content)
                .build();

        chatMessageRepository.save(message);
    }

    @Override
    public void saveAssistantMessage(
            Long sessionId,
            String content) {

        ChatMessage message = ChatMessage.builder()
                .sessionId(sessionId)
                .role(MessageRole.ASSISTANT)
                .content(content)
                .build();

        chatMessageRepository.save(message);
    }
}