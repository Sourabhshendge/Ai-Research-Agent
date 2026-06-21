package com.sourabh.document.dto;

import lombok.Builder;

@Builder
public record DocumentStatsResponse(
        long totalDocuments,
        long totalChunks,
        double averageChunksPerDocument,
        long totalStorageBytes
) {
}