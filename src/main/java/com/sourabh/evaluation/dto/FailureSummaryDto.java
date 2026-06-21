package com.sourabh.evaluation.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FailureSummaryDto {

    private long success;

    private long miss;

    private long lowRank;

    private long partialMatch;
}