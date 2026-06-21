package com.sourabh.evaluation.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CategorySummaryResponseDto(

        String retriever,

        List<CategoryMetricsDto> categories

) {
}