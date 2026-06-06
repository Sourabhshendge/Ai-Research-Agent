package com.sourabh.chat.dto;

import com.sourabh.common.response.SourceDto;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseDto {

    private String answer;
    private List<SourceDto> sources;
}
