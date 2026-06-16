package com.sourabh.evaluation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EvaluationSummaryDto {

    private double averageRecall;

    private double averagePrecision;

    private double averageMrr;

    private double averageNdcg;

    private int totalQuestions;
}
