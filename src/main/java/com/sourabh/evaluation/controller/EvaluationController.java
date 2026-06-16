package com.sourabh.evaluation.controller;


import com.sourabh.common.response.ApiResponse;
import com.sourabh.evaluation.dto.BenchmarkResponseDto;
import com.sourabh.evaluation.dto.EvaluationMetricsDto;
import com.sourabh.evaluation.dto.EvaluationResult;
import com.sourabh.evaluation.dto.EvaluationSummaryDto;
import com.sourabh.evaluation.service.EvaluationBenchmarkService;
import com.sourabh.evaluation.service.RetrievalEvaluationService;
import com.sourabh.search.document.DocumentChunkIndex;
import com.sourabh.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/evaluation")
@RequiredArgsConstructor
public class EvaluationController {

    private final RetrievalEvaluationService evaluationService;
    private final EvaluationBenchmarkService benchmarkService;
    private final SearchService searchService;

    @PostMapping("/run")
    public ApiResponse<List<EvaluationResult>> runEvaluation() {

        return ApiResponse.success(
                evaluationService.runEvaluation()
        );
    }

    @GetMapping("/recall")
    public ApiResponse<List<EvaluationMetricsDto>>
    runRecallEvaluation() {

        return ApiResponse.success(
                evaluationService.runMetricsEvaluation()
        );
    }

    @GetMapping("/summary")
    public ApiResponse<EvaluationSummaryDto>
    runSummaryEvaluation() {

        return ApiResponse.success(
                evaluationService.runSummaryEvaluation()
        );
    }

    @GetMapping("/benchmark")
    public ApiResponse<BenchmarkResponseDto>
    runBenchmark() {

        return ApiResponse.success(
                benchmarkService.runBenchmark()
        );
    }

    @GetMapping("/bm25-debug")
    public List<DocumentChunkIndex> bm25Debug() {

        return searchService.searchChunks(
                "database",
                10
        );
    }
}