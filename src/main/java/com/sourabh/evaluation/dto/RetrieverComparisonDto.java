package com.sourabh.evaluation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RetrieverComparisonDto {

    private String question;

    private Integer hybridRank;

    private Integer hybridRerankRank;

    private Integer improvement;
}