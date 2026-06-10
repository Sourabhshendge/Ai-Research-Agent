package com.sourabh.rerank.dto;


import lombok.Builder;

@Builder
public record RerankCandidate(
        String content,
        String fileName,
        Long documentId,
        Integer chunkIndex,
        double retrievalScore
) {}
