package com.sourabh.evaluation.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CategoryFailureDetailDto {

    private String question;

    private List<String> expectedChunkIds;

    private List<String> retrievedChunkIds;

    private Integer bestRelevantRank;

    private FailureType failureType;
}