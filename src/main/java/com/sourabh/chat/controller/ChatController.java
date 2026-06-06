package com.sourabh.chat.controller;

import com.sourabh.chat.dto.ChatRequest;
import com.sourabh.chat.dto.ChatResponseDto;
import com.sourabh.chat.service.ChatService;
import com.sourabh.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/ask")
    public ApiResponse<ChatResponseDto> ask(
            @RequestBody ChatRequest request
    ) {

        return ApiResponse.success(
                chatService.ask(
                        request.getSessionId(),
                        request.getQuestion()
                )
        );
    }

    @PostMapping(
            value = "/stream",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public Flux<ServerSentEvent<String>> stream(
            @RequestBody ChatRequest request) {

        return chatService.stream(
                request.getSessionId(),
                request.getQuestion()
        );
    }
}
