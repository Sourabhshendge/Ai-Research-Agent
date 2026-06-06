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

STRICT RULES:

- Answer ONLY using the retrieved context.
- Do NOT use outside knowledge.
- Do NOT infer skills.
- Do NOT guess.
- Do NOT add technologies that are not explicitly present.
- If information is missing, say:
  "I could not find that information in the provided documents."

QUESTION:
%s

CONTEXT:
%s

ANSWER:
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