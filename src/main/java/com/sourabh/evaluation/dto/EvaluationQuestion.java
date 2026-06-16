package com.sourabh.evaluation.dto;

import lombok.Data;

import java.util.List;

@Data
public class EvaluationQuestion {

    private String question;

    private List<String> relevantChunkIds;

}