package com.sourabh.document.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ListDocumentResponse(
        Long id,
        String fileName,
        String fileType,
        Long fileSize,
        Integer chunkCount,
        Instant uploadedAt
) {
}