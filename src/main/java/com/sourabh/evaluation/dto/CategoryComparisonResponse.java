package com.sourabh.evaluation.dto;

import lombok.Builder;

@Builder
public record CategoryComparisonResponse(
        QuestionCategory category,

        double hybridRecall,
        double rerankRecall,
        double recallImprovement,

        double hybridMrr,
        double rerankMrr,
        double mrrImprovement,

        double hybridNdcg,
        double rerankNdcg,
        double ndcgImprovement,

        double hybridMap,
        double rerankMap,
        double mapImprovement
) {
}