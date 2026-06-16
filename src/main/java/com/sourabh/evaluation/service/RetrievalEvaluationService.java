package com.sourabh.evaluation.service;

import com.sourabh.evaluation.dto.EvaluationMetricsDto;
import com.sourabh.evaluation.dto.EvaluationQuestion;
import com.sourabh.evaluation.dto.EvaluationResult;
import com.sourabh.evaluation.dto.EvaluationSummaryDto;
import com.sourabh.evaluation.metrics.MrrCalculator;
import com.sourabh.evaluation.metrics.NdcgCalculator;
import com.sourabh.evaluation.metrics.PrecisionCalculator;
import com.sourabh.evaluation.metrics.RecallCalculator;
import com.sourabh.retrieval.dto.HybridResultDto;
import com.sourabh.retrieval.service.HybridSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetrievalEvaluationService {

    private final EvaluationDatasetService datasetService;
    private final HybridSearchService hybridSearchService;
    private final RecallCalculator recallCalculator;
    private final PrecisionCalculator precisionCalculator;
    private final MrrCalculator mrrCalculator;
    private final NdcgCalculator ndcgCalculator;

    public List<EvaluationResult> runEvaluation() {

        List<EvaluationQuestion> dataset =
                datasetService.loadDataset();

        List<EvaluationResult> results =
                new ArrayList<>();

        for (EvaluationQuestion question : dataset) {

            List<HybridResultDto> retrievedResults =
                    hybridSearchService.search(
                            question.getQuestion(),
                            20
                    );

            List<String> retrievedChunkIds =
                    retrievedResults.stream()
                            .map(result ->
                                    result.documentId()
                                            + "_"
                                            + result.chunkIndex()
                            )
                            .toList();

            List<String> retrievedContents =
                    retrievedResults.stream()
                            .map(HybridResultDto::content)
                            .toList();



            EvaluationResult result =
                    EvaluationResult.builder()
                            .question(question.getQuestion())
                            .relevantChunkIds(
                                    question.getRelevantChunkIds()
                            )
                            .retrievedChunkIds(
                                    retrievedChunkIds
                            )
                            .retrievedContents(
                                    retrievedContents
                            )
                            .build();

            results.add(result);
        }

        return results;
    }

    public List<EvaluationMetricsDto>
    runMetricsEvaluation() {

        List<EvaluationResult> results =
                runEvaluation();

        return results.stream()
                .map(result -> {

                    double recall =
                            recallCalculator.calculate(
                                    result.getRelevantChunkIds(),
                                    result.getRetrievedChunkIds()
                            );

                    double precision =
                            precisionCalculator.calculate(
                                    result.getRelevantChunkIds(),
                                    result.getRetrievedChunkIds()
                            );

                    double mrr =
                            mrrCalculator.calculate(
                                    result.getRelevantChunkIds(),
                                    result.getRetrievedChunkIds()
                            );

                    double ndcg =
                            ndcgCalculator.calculate(
                                    result.getRelevantChunkIds(),
                                    result.getRetrievedChunkIds()
                            );

                    return EvaluationMetricsDto.builder()
                            .question(result.getQuestion())
                            .recall(recall)
                            .precision(precision)
                            .mrr(mrr)
                            .ndcg(ndcg)
                            .build();
                })
                .toList();
    }

    public EvaluationSummaryDto runSummaryEvaluation() {

        List<EvaluationMetricsDto> metrics =
                runMetricsEvaluation();

        double averageRecall =
                metrics.stream()
                        .mapToDouble(
                                EvaluationMetricsDto::getRecall
                        )
                        .average()
                        .orElse(0.0);

        double averagePrecision =
                metrics.stream()
                        .mapToDouble(
                                EvaluationMetricsDto::getPrecision
                        )
                        .average()
                        .orElse(0.0);

        double averageMrr =
                metrics.stream()
                        .mapToDouble(
                                EvaluationMetricsDto::getMrr
                        )
                        .average()
                        .orElse(0.0);

        double averageNdcg =
                metrics.stream()
                        .mapToDouble(
                                EvaluationMetricsDto::getNdcg
                        )
                        .average()
                        .orElse(0.0);

        return EvaluationSummaryDto.builder()
                .averageRecall(averageRecall)
                .averagePrecision(averagePrecision)
                .averageMrr(averageMrr)
                .averageNdcg(averageNdcg)
                .totalQuestions(metrics.size())
                .build();
    }
}
