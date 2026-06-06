package com.sourabh.chat.rewrite;

import com.sourabh.memory.dto.ConversationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class QueryRewriterServiceImpl
        implements QueryRewriterService {

    @Override
    public String rewrite(
            String question,
            List<ConversationMessage> history
    ) {

        if (history.size() < 2) {
            return question;
        }

        String lowerQuestion =
                question.toLowerCase();

        boolean followUpQuestion =
                lowerQuestion.contains("what about")
                        || lowerQuestion.contains("it")
                        || lowerQuestion.contains("they")
                        || lowerQuestion.contains("them")
                        || lowerQuestion.contains("his")
                        || lowerQuestion.contains("her")
                        || lowerQuestion.contains("that");

        if (!followUpQuestion) {
            return question;
        }

        String previousUserQuestion =
                history.stream()
                        .filter(m ->
                                "USER".equalsIgnoreCase(
                                        m.getRole()
                                ))
                        .reduce((first, second) -> second)
                        .map(ConversationMessage::getContent)
                        .orElse(null);

        for (int i = history.size() - 1; i >= 0; i--) {

            ConversationMessage message =
                    history.get(i);

            if ("USER".equalsIgnoreCase(
                    message.getRole()
            )) {

                previousUserQuestion =
                        message.getContent();

                break;
            }
        }

        if (previousUserQuestion == null) {
            return question;
        }

        String rewrittenQuestion =
                previousUserQuestion
                        + " "
                        + question;

        log.info(
                "Query rewritten from [{}] to [{}]",
                question,
                rewrittenQuestion
        );

        return rewrittenQuestion;
    }
}