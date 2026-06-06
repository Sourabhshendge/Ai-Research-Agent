package com.sourabh.retrieval.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RetrievalEvaluationResponse {

    private String question;

    private Integer totalChunks;

    private List<RetrievedChunkDto> results;
}
