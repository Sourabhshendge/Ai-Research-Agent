package com.sourabh.memory.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationMessage {

    private String role;
    private String content;
}