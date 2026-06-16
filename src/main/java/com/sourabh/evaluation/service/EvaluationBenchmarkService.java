package com.sourabh.evaluation.service;

import com.sourabh.evaluation.dto.BenchmarkResponseDto;
import com.sourabh.evaluation.dto.EvaluationQuestion;
import com.sourabh.evaluation.dto.EvaluationSummaryDto;
import com.sourabh.evaluation.metrics.MrrCalculator;
import com.sourabh.evaluation.metrics.NdcgCalculator;
import com.sourabh.evaluation.metrics.PrecisionCalculator;
import com.sourabh.evaluation.metrics.RecallCalculator;
import com.sourabh.evaluation.retriever.EvaluationRetriever;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationBenchmarkService {

    private final RecallCalculator recallCalculator;
    private final PrecisionCalculator precisionCalculator;
    private final MrrCalculator mrrCalculator;
    private final NdcgCalculator ndcgCalculator;

    private final EvaluationRetriever bm25Retriever;
    private final EvaluationRetriever vectorRetriever;
    private final EvaluationRetriever hybridRetriever;

    private final EvaluationDatasetService datasetService;

    public EvaluationBenchmarkService(
            RecallCalculator recallCalculator,
            PrecisionCalculator precisionCalculator,
            MrrCalculator mrrCalculator,
            NdcgCalculator ndcgCalculator,
            @Qualifier("bm25Retriever")
            EvaluationRetriever bm25Retriever,
            @Qualifier("vectorRetriever")
            EvaluationRetriever vectorRetriever,
            @Qualifier("hybridRetriever")
            EvaluationRetriever hybridRetriever,
            EvaluationDatasetService datasetService
    ) {
        this.recallCalculator = recallCalculator;
        this.precisionCalculator = precisionCalculator;
        this.mrrCalculator = mrrCalculator;
        this.ndcgCalculator = ndcgCalculator;
        this.bm25Retriever = bm25Retriever;
        this.vectorRetriever = vectorRetriever;
        this.hybridRetriever = hybridRetriever;
        this.datasetService = datasetService;
    }

    public EvaluationSummaryDto evaluate(
            EvaluationRetriever retriever
    ) {

        List<EvaluationQuestion> dataset =
                datasetService.loadDataset();

        double totalRecall = 0.0;
        double totalPrecision = 0.0;
        double totalMrr = 0.0;
        double totalNdcg = 0.0;

        for (EvaluationQuestion question : dataset) {

            List<String> retrievedChunkIds =
                    retriever.retrieve(
                            question.getQuestion(),
                            20
                    );

            System.out.println(
                    "Question = "
                            + question.getQuestion()
            );

            System.out.println(
                    "Relevant = "
                            + question.getRelevantChunkIds()
            );

            System.out.println(
                    "Retrieved = "
                            + retrievedChunkIds
            );

            totalRecall +=
                    recallCalculator.calculate(
                            question.getRelevantChunkIds(),
                            retrievedChunkIds
                    );

            totalPrecision +=
                    precisionCalculator.calculate(
                            question.getRelevantChunkIds(),
                            retrievedChunkIds
                    );

            totalMrr +=
                    mrrCalculator.calculate(
                            question.getRelevantChunkIds(),
                            retrievedChunkIds
                    );

            totalNdcg +=
                    ndcgCalculator.calculate(
                            question.getRelevantChunkIds(),
                            retrievedChunkIds
                    );
        }

        int totalQuestions = dataset.size();



        return EvaluationSummaryDto.builder()
                .averageRecall(
                        totalRecall / totalQuestions
                )
                .averagePrecision(
                        totalPrecision / totalQuestions
                )
                .averageMrr(
                        totalMrr / totalQuestions
                )
                .averageNdcg(
                        totalNdcg / totalQuestions
                )
                .totalQuestions(totalQuestions)
                .build();
    }

    public BenchmarkResponseDto runBenchmark() {

        EvaluationSummaryDto bm25 =
                evaluate(bm25Retriever);

        EvaluationSummaryDto vector =
                evaluate(vectorRetriever);

        EvaluationSummaryDto hybrid =
                evaluate(hybridRetriever);

        return BenchmarkResponseDto.builder()
                .bm25(bm25)
                .vector(vector)
                .hybrid(hybrid)
                .build();
    }
}