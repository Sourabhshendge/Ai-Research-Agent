package com.sourabh.retrieval.dto;

public record HybridResultDto(
        String chunkId,
        String content,
        String documentName,
        Long documentId,
        Integer chunkIndex,
        double score
) {
}