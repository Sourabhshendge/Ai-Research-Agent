package com.sourabh.evaluation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryMetricsDto {

    private QuestionCategory category;

    private double recall;

    private double precision;

    private double mrr;

    private double ndcg;

    private double map;

    private double hitRate;

    private int totalQuestions;
}