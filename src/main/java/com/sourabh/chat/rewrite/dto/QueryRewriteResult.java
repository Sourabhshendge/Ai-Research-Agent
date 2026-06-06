package com.sourabh.chat.rewrite.dto;

public record QueryRewriteResult(
        String originalQuestion,
        String rewrittenQuestion
) {
}