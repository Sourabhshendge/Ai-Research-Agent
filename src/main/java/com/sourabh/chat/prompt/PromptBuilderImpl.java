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
You are a retrieval assistant.

Answer only from the provided context.

If the answer is not explicitly present in the context, reply exactly:

I could not find that information in the provided documents.

Question:
%s

Context:
%s

Answer:
"""
                .formatted(
                        question,
                        context
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