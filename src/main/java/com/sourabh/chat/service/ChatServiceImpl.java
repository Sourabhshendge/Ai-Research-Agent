package com.sourabh.chat.service;

import com.sourabh.chat.dto.ChatResponseDto;
import com.sourabh.chat.prompt.PromptBuilder;
import com.sourabh.common.response.SourceDto;
import com.sourabh.memory.dto.ConversationMessage;
import com.sourabh.memory.service.ConversationMemoryService;
import com.sourabh.retrieval.config.RetrievalProperties;
import com.sourabh.retrieval.dto.SearchResultDto;
import com.sourabh.retrieval.service.RetrievalService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {


    private final ChatModel chatModel;
    private final RetrievalService retrievalService;
    private final PromptBuilder promptBuilder;
    private final RetrievalProperties retrievalProperties;
    private final ChatMessageService chatMessageService;
    private final ConversationMemoryService conversationMemoryService;

    @Override
    public ChatResponseDto ask(
            Long sessionId,
            String question) {

        List<ConversationMessage> history =
                conversationMemoryService
                        .getConversationHistory(sessionId);

        chatMessageService.saveUserMessage(
                sessionId,
                question
        );

        List<SearchResultDto> searchResults =
                retrieveContext(question);

        String promptText =
                promptBuilder.buildPrompt(
                        question,
                        searchResults,
                        history
                );

        System.out.println(promptText);

        Prompt prompt = new Prompt(promptText);

        ChatResponse response =
                chatModel.call(prompt);

        String answer = response.getResult()
                .getOutput()
                .getText();

        chatMessageService.saveAssistantMessage(
                sessionId,
                answer
        );

        List<SourceDto> sources =
                searchResults.stream()
                        .map(result -> SourceDto.builder()
                                .documentId(result.getDocumentId())
                                .fileName(result.getFileName())
                                .chunkIndex(result.getChunkIndex())
                                .score(result.getScore())
                                .build())
                        .toList();

        return ChatResponseDto.builder()
                .answer(answer)
                .sources(sources)
                .build();
    }

    @Override
    public Flux<ServerSentEvent<String>> stream(
            Long sessionId,
            String question
    ) {

        List<ConversationMessage> history =
                conversationMemoryService
                        .getConversationHistory(sessionId);

        chatMessageService.saveUserMessage(
                sessionId,
                question
        );

        List<SearchResultDto> searchResults =
                retrieveContext(question);

        if (searchResults.isEmpty()) {

            String answer =
                    "No relevant information was found in the uploaded documents.";

            chatMessageService.saveAssistantMessage(
                    sessionId,
                    answer
            );

            return Flux.just(
                    ServerSentEvent.<String>builder()
                            .event("message")
                            .data(answer)
                            .build()
            );
        }

        String promptText =
                promptBuilder.buildPrompt(
                        question,
                        searchResults,
                        history
                );

        Prompt prompt = new Prompt(promptText);

        StringBuilder assistantResponse =
                new StringBuilder();

        return chatModel.stream(prompt)
                .map(ChatResponse::getResult)
                .map(result -> result.getOutput().getText())
                .doOnNext(assistantResponse::append)
                .map(chunk ->
                        ServerSentEvent.builder(chunk)
                                .build()
                )
                .doOnComplete(() ->
                        chatMessageService.saveAssistantMessage(
                                sessionId,
                                assistantResponse.toString()
                        )
                );
    }

    private List<SearchResultDto> retrieveContext(String question) {

        return retrievalService.search(
                question,
                retrievalProperties.topK()
        );
    }
}