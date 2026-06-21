package com.sourabh.document.dto;

import com.sourabh.document.entity.DocumentStatus;
import lombok.Builder;

@Builder
public record DocumentResponse(
        Long id,
        String fileName,
        String fileType,
        Long fileSize,
        DocumentStatus status
) {
}