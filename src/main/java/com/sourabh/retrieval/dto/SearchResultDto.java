package com.sourabh.retrieval.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultDto {

    private String content;

    private Double score;

    private Long documentId;

    private String fileName;

    private Integer chunkIndex;

    private String vectorId;
}