package com.sourabh.chat.rewrite;

import com.sourabh.memory.dto.ConversationMessage;

import java.util.List;

public interface QueryRewriterService {

    String rewrite(
            String question,
            List<ConversationMessage> history
    );
}