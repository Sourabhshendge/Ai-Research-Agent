package com.sourabh.evaluation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryFailureDto {


    private QuestionCategory category;

    private long success;

    private long lowRank;

    private long partialMatch;

    private long miss;

    private long totalQuestions;


}
