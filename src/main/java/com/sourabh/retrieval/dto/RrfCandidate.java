package com.sourabh.retrieval.dto;

public record RrfCandidate(
        String chunkId,
        String content,
        String documentName,
        Long documentId,
        Integer chunkIndex,
        double score
) {
}