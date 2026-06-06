package com.sourabh.document.dto;

import lombok.Builder;

@Builder
public record DocumentResponse(
        Long id,
        String fileName,
        String fileType,
        Long fileSize
) {
}