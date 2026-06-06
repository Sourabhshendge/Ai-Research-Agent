package com.sourabh.chat.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionResponse {

    private Long id;

    private String title;
}