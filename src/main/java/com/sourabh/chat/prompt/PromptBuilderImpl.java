package com.sourabh.chat.prompt;

import com.sourabh.memory.dto.ConversationMessage;
import com.sourabh.retrieval.dto.SearchResultDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PromptBuilderImpl implements PromptBuilder {

    @Override
    public String buildPrompt(
            String question,
            List<SearchResultDto> chunks,
            List<ConversationMessage> history
    ) {

        String context = buildContext(chunks);

        String conversationHistory =
                buildHistorySection(history);

        return """
                You are a helpful AI assistant.

                IMPORTANT RULES:
                
                1. First check conversation history.
                
                2. If the answer is available in conversation history,
                   answer using conversation history.
                
                3. If the answer is not available in conversation history,
                   use retrieved context.
                
                4. Retrieved context should be used only when
                   conversation history does not contain the answer.
                
                5. Do not invent information.
                
                6. If neither conversation history nor retrieved context
                   contains the answer, respond:
                
                   "I could not find that information in the provided documents or conversation history."

                CONVERSATION HISTORY
                ====================
                ...
                
                CURRENT QUESTION
                ====================
                ...
                
                RETRIEVED CONTEXT
                ====================
                ...


                Answer:
                """
                .formatted(
                        conversationHistory,
                        context,
                        question
                );
    }

    private String buildHistorySection(
            List<ConversationMessage> history
    ) {

        if (history == null || history.isEmpty()) {
            return "No previous conversation.";
        }

        StringBuilder sb = new StringBuilder();

        for (ConversationMessage message : history) {

            sb.append(message.getRole())
                    .append(": ")
                    .append(message.getContent())
                    .append("\n");
        }

        return sb.toString();
    }

    private String buildContext(
            List<SearchResultDto> chunks
    ) {

        if (chunks == null || chunks.isEmpty()) {
            return "No relevant context found.";
        }

        StringBuilder sb = new StringBuilder();

        for (SearchResultDto chunk : chunks) {

            sb.append(chunk.getContent())
                    .append("\n\n");
        }

        return sb.toString();
    }
}