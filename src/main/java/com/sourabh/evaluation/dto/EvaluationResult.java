package com.sourabh.evaluation.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EvaluationResult {

    private String question;

    private List<String> relevantChunkIds;

    private List<String> retrievedChunkIds;

    private List<String> retrievedContents;

}