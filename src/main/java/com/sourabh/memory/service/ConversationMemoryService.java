package com.sourabh.memory.service;

import com.sourabh.memory.dto.ConversationMessage;

import java.util.List;

public interface ConversationMemoryService {

    List<ConversationMessage> getConversationHistory(
            Long sessionId
    );
}