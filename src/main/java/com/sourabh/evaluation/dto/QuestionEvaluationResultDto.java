package com.sourabh.evaluation.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record QuestionEvaluationResultDto(

        String question,

        List<String> relevantChunkIds,

        List<String> retrievedChunkIds,

        double recall,

        double precision,

        double mrr,

        double ndcg,

        double hitRate,

        double map

) {
}