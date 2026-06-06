package com.sourabh.memory.service;

import com.sourabh.chat.entity.ChatMessage;
import com.sourabh.chat.repository.ChatMessageRepository;
import com.sourabh.config.ChatProperties;
import com.sourabh.memory.dto.ConversationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationMemoryServiceImpl
        implements ConversationMemoryService {

    private final ChatMessageRepository repository;
    private final ChatProperties chatProperties;

    @Override
    public List<ConversationMessage> getConversationHistory(
            Long sessionId
    ) {

        Pageable pageable =
                PageRequest.of(
                        0,
                        chatProperties.getMemoryWindow()
                );

        List<ChatMessage> messages =
                new ArrayList<>(
                        repository
                                .findBySessionIdOrderByCreatedAtDesc(
                                        sessionId,
                                        pageable
                                )
                                .getContent()
                );

        Collections.reverse(messages);

        return messages.stream()
                .map(this::toConversationMessage)
                .toList();
    }

    private ConversationMessage toConversationMessage(
            ChatMessage message
    ) {

        return ConversationMessage.builder()
                .role(message.getRole().name())
                .content(message.getContent())
                .build();
    }
}