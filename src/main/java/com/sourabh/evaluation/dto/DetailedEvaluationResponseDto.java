package com.sourabh.evaluation.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record DetailedEvaluationResponseDto(

        String retriever,

        EvaluationSummaryDto summary,

        List<QuestionEvaluationResultDto> questions

) {
}