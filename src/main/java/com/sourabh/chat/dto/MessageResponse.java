package com.sourabh.chat.dto;

import com.sourabh.chat.entity.MessageRole;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {

    private MessageRole role;

    private String content;
}