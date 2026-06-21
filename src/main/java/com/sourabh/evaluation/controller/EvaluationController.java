package com.sourabh.evaluation.controller;


import com.sourabh.common.response.ApiResponse;
import com.sourabh.evaluation.dto.*;
import com.sourabh.evaluation.retriever.EvaluationRetriever;
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

    private final EvaluationRetriever hybridRetriever;

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

    @GetMapping("/detailed")
    public ApiResponse<DetailedEvaluationResponseDto>
    getDetailedReport(
            @RequestParam String retriever
    ) {

        return ApiResponse.success(
                benchmarkService.getDetailedReport(
                        retriever
                )
        );
    }

    @GetMapping("/failures")
    public ApiResponse<List<FailureAnalysisDto>>
    getFailures() {

        return ApiResponse.success(
                benchmarkService.getFailures(
                        hybridRetriever
                )
        );
    }

    @GetMapping("/failure-summary")
    public ApiResponse<FailureSummaryDto>
    getFailureSummary() {

        return ApiResponse.success(
                benchmarkService.getFailureSummary(
                        hybridRetriever
                )
        );
    }

    @GetMapping("/compare")
    public ApiResponse<List<RetrieverComparisonDto>>
    compareRetrievers() {

        return ApiResponse.success(
                benchmarkService.compareHybridVsRerank()
        );
    }

    @GetMapping("/category-summary")
    public ApiResponse<CategorySummaryResponseDto>
    getCategorySummary(
            @RequestParam String retriever
    ) {

        return ApiResponse.success(
                benchmarkService.getCategorySummary(
                        retriever
                )
        );
    }

    @GetMapping("/category-comparison")
    public ApiResponse<List<CategoryComparisonResponse>>
    getCategoryComparison() {

        return ApiResponse.success(
                benchmarkService.compareCategories()
        );
    }

    @GetMapping("/category-failures")
    public ApiResponse<List<CategoryFailureDto>>
    getCategoryFailures() {

        return ApiResponse.success(
                benchmarkService.getCategoryFailures()
        );


    }

    @GetMapping("/category-details")
    public ApiResponse<List<CategoryFailureDetailDto>>
    getCategoryDetails(
            @RequestParam QuestionCategory category
    ) {

        return ApiResponse.success(
                benchmarkService.getCategoryDetails(
                        category
                )
        );
    }

}