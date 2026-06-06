package com.sourabh.chat.prompt;


import com.sourabh.memory.dto.ConversationMessage;
import com.sourabh.retrieval.dto.SearchResultDto;

import java.util.List;

public interface PromptBuilder {

    String buildPrompt(
            String question,
            List<SearchResultDto> chunks,
            List<ConversationMessage> history
    );
}
