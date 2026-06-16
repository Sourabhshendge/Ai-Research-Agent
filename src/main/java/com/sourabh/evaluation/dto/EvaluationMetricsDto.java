package com.sourabh.evaluation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EvaluationMetricsDto {

    private String question;

    private double recall;

    private double precision;

    private double mrr;

    private double ndcg;
}