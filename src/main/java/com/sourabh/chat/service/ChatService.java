package com.sourabh.chat.service;

import com.sourabh.chat.dto.ChatResponseDto;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public interface ChatService {

    ChatResponseDto ask(
            Long sessionId,
            String question
    );

    Flux<ServerSentEvent<String>> stream(
            Long sessionId,
            String question
    );
}
