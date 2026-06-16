package com.sourabh.evaluation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BenchmarkResponseDto {

    private EvaluationSummaryDto bm25;

    private EvaluationSummaryDto vector;

    private EvaluationSummaryDto hybrid;
}
