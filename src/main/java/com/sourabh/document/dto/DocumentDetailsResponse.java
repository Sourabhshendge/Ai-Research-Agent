package com.sourabh.document.dto;

import com.sourabh.document.entity.DocumentStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public record DocumentDetailsResponse(
        Long id,
        String fileName,
        String fileType,
        Long fileSize,
        String fileHash,
        Integer chunkCount,
        Instant uploadedAt,
        DocumentStatus status,
        String failureReason
) {
}