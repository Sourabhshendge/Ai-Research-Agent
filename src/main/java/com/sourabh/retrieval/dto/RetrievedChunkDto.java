package com.sourabh.retrieval.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RetrievedChunkDto {

    private Long documentId;

    private String fileName;

    private Integer chunkIndex;

    private Double score;

    private String content;
}