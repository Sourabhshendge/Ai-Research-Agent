package com.sourabh.chat.repository;

import com.sourabh.chat.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository
        extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findBySessionIdOrderByCreatedAtAsc(
            Long sessionId
    );

    Page<ChatMessage> findBySessionIdOrderByCreatedAtDesc(
            Long sessionId,
            Pageable pageable
    );
}