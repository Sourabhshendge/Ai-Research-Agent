package com.sourabh.rerank.dto;


public record CrossEncoderInput(
        long[] inputIds,
        long[] attentionMask,
        long[] tokenTypeIds
) {
}