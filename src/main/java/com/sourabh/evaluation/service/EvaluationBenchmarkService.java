package com.sourabh.evaluation.service;

import com.sourabh.evaluation.dto.*;
import com.sourabh.evaluation.metrics.*;
import com.sourabh.evaluation.retriever.EvaluationRetriever;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationBenchmarkService {

    private final RecallCalculator recallCalculator;
    private final PrecisionCalculator precisionCalculator;
    private final MrrCalculator mrrCalculator;
    private final NdcgCalculator ndcgCalculator;
    private final EvaluationRetriever bm25Retriever;
    private final EvaluationRetriever vectorRetriever;
    private final EvaluationRetriever hybridRetriever;
    private final EvaluationDatasetService datasetService;
    private final EvaluationRetriever hybridRerankRetriever;
    private final HitRateCalculator hitRateCalculator;
    private final MapCalculator mapCalculator;


    private static final int RETRIEVAL_K = 20;
    private static final int SUCCESS_THRESHOLD = 5;

    public EvaluationSummaryDto evaluate(
            EvaluationRetriever retriever
    ) {

        List<EvaluationQuestion> dataset =
                datasetService.loadDataset();

        double totalRecall = 0.0;
        double totalPrecision = 0.0;
        double totalMrr = 0.0;
        double totalNdcg = 0.0;
        double totalHitRate = 0.0;
        double totalMap = 0.0;

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

            totalHitRate +=
                    hitRateCalculator.calculate(
                            question.getRelevantChunkIds(),
                            retrievedChunkIds
                    );

            totalMap +=
                    mapCalculator.calculate(
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
                .averageHitRate(
                        totalHitRate / totalQuestions
                )
                .averageMap(
                        totalMap / totalQuestions
                )
                .totalQuestions(totalQuestions)
                .build();
    }

    public DetailedEvaluationResponseDto evaluateDetailed(
            EvaluationRetriever retriever,
            String retrieverName
    ) {

        List<EvaluationQuestion> dataset =
                datasetService.loadDataset();

        List<QuestionEvaluationResultDto> questionResults =
                new ArrayList<>();

        double totalRecall = 0.0;
        double totalPrecision = 0.0;
        double totalMrr = 0.0;
        double totalNdcg = 0.0;
        double totalHitRate = 0.0;
        double totalMap = 0.0;

        for (EvaluationQuestion question : dataset) {

            List<String> retrievedChunkIds =
                    retriever.retrieve(
                            question.getQuestion(),
                            20
                    );

            double recall =
                    recallCalculator.calculate(
                            question.getRelevantChunkIds(),
                            retrievedChunkIds
                    );

            double precision =
                    precisionCalculator.calculate(
                            question.getRelevantChunkIds(),
                            retrievedChunkIds
                    );

            double mrr =
                    mrrCalculator.calculate(
                            question.getRelevantChunkIds(),
                            retrievedChunkIds
                    );

            double ndcg =
                    ndcgCalculator.calculate(
                            question.getRelevantChunkIds(),
                            retrievedChunkIds
                    );

            double hitRate =
                    hitRateCalculator.calculate(
                            question.getRelevantChunkIds(),
                            retrievedChunkIds
                    );

            double map =
                    mapCalculator.calculate(
                            question.getRelevantChunkIds(),
                            retrievedChunkIds
                    );

            totalRecall += recall;
            totalPrecision += precision;
            totalMrr += mrr;
            totalNdcg += ndcg;
            totalHitRate += hitRate;
            totalMap += map;

            questionResults.add(
                    QuestionEvaluationResultDto.builder()
                            .question(
                                    question.getQuestion()
                            )
                            .relevantChunkIds(
                                    question.getRelevantChunkIds()
                            )
                            .retrievedChunkIds(
                                    retrievedChunkIds
                            )
                            .recall(recall)
                            .precision(precision)
                            .mrr(mrr)
                            .ndcg(ndcg)
                            .hitRate(hitRate)
                            .map(map)
                            .build()
            );
        }

        int totalQuestions = dataset.size();

        EvaluationSummaryDto summary =
                EvaluationSummaryDto.builder()
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
                        .averageHitRate(
                                totalHitRate / totalQuestions
                        )
                        .averageMap(
                                totalMap / totalQuestions
                        )
                        .totalQuestions(
                                totalQuestions
                        )
                        .build();

        return DetailedEvaluationResponseDto.builder()
                .retriever(retrieverName)
                .summary(summary)
                .questions(questionResults)
                .build();
    }

    public BenchmarkResponseDto runBenchmark() {

        EvaluationSummaryDto bm25 =
                evaluate(bm25Retriever);

        EvaluationSummaryDto vector =
                evaluate(vectorRetriever);

        EvaluationSummaryDto hybrid =
                evaluate(hybridRetriever);

        EvaluationSummaryDto hybridRerank =
                evaluate(hybridRerankRetriever);

        return BenchmarkResponseDto.builder()
                .bm25(bm25)
                .vector(vector)
                .hybrid(hybrid)
                .hybridRerank(hybridRerank)
                .build();
    }

    public DetailedEvaluationResponseDto
    getDetailedReport(
            String retriever
    ) {

        return switch (retriever.toLowerCase()) {

            case "bm25" ->
                    evaluateDetailed(
                            bm25Retriever,
                            "BM25"
                    );

            case "vector" ->
                    evaluateDetailed(
                            vectorRetriever,
                            "VECTOR"
                    );

            case "hybrid" ->
                    evaluateDetailed(
                            hybridRetriever,
                            "HYBRID"
                    );

            case "hybridrerank" ->
                    evaluateDetailed(
                            hybridRerankRetriever,
                            "HYBRID_RERANK"
                    );

            default ->
                    throw new IllegalArgumentException(
                            "Unknown retriever: "
                                    + retriever
                    );
        };
    }

    private FailureType determineFailureType(
            List<String> relevant,
            List<String> retrieved
    ) {

        Integer bestRank =
                findBestRelevantRank(
                        relevant,
                        retrieved
                );

        if (bestRank != null) {

            if (bestRank <= SUCCESS_THRESHOLD) {
                return FailureType.SUCCESS;
            }

            return FailureType.LOW_RANK;
        }

        String expectedDocument =
                relevant.getFirst()
                        .split("_")[0];

        boolean sameDocumentRetrieved =
                retrieved.stream()
                        .map(id -> id.split("_")[0])
                        .anyMatch(expectedDocument::equals);

        if (sameDocumentRetrieved) {
            return FailureType.PARTIAL_MATCH;
        }

        return FailureType.MISS;
    }

    private Integer findBestRelevantRank(
            List<String> relevant,
            List<String> retrieved
    ) {

        int bestRank = Integer.MAX_VALUE;

        for (String chunkId : relevant) {

            int index =
                    retrieved.indexOf(chunkId);

            if (index >= 0) {

                bestRank =
                        Math.min(
                                bestRank,
                                index + 1
                        );
            }
        }

        return bestRank == Integer.MAX_VALUE
                ? null
                : bestRank;
    }
    public List<FailureAnalysisDto>
    getFailures(
            EvaluationRetriever retriever
    ) {

        List<EvaluationQuestion> dataset =
                datasetService.loadDataset();

        List<FailureAnalysisDto> failures =
                new ArrayList<>();

        for (EvaluationQuestion question : dataset) {

            List<String> retrievedChunkIds =
                    retriever.retrieve(
                            question.getQuestion(),
                            RETRIEVAL_K
                    );

            FailureType failureType =
                    determineFailureType(
                            question.getRelevantChunkIds(),
                            retrievedChunkIds
                    );

            if (failureType == FailureType.SUCCESS) {
                continue;
            }

            failures.add(
                    FailureAnalysisDto.builder()
                            .question(
                                    question.getQuestion()
                            )
                            .expectedChunkIds(
                                    question.getRelevantChunkIds()
                            )
                            .retrievedChunkIds(
                                    retrievedChunkIds
                            )
                            .bestRelevantRank(
                                    findBestRelevantRank(
                                            question.getRelevantChunkIds(),
                                            retrievedChunkIds
                                    )
                            )
                            .failureType(
                                    failureType
                            )
                            .build()
            );
        }

        return failures;
    }

    public FailureSummaryDto
    getFailureSummary(
            EvaluationRetriever retriever
    ) {

        List<FailureAnalysisDto> failures =
                getFailures(retriever);

        long miss =
                failures.stream()
                        .filter(f ->
                                f.getFailureType()
                                        == FailureType.MISS)
                        .count();

        long lowRank =
                failures.stream()
                        .filter(f ->
                                f.getFailureType()
                                        == FailureType.LOW_RANK)
                        .count();

        long partialMatch =
                failures.stream()
                        .filter(f ->
                                f.getFailureType()
                                        == FailureType.PARTIAL_MATCH)
                        .count();

        int totalQuestions =
                datasetService.loadDataset().size();

        long success =
                totalQuestions
                        - failures.size();

        return FailureSummaryDto.builder()
                .success(success)
                .miss(miss)
                .lowRank(lowRank)
                .partialMatch(partialMatch)
                .build();
    }


    public List<RetrieverComparisonDto>
    compareHybridVsRerank() {

        List<EvaluationQuestion> dataset =
                datasetService.loadDataset();

        List<RetrieverComparisonDto> results =
                new ArrayList<>();

        for (EvaluationQuestion question : dataset) {

            List<String> hybridResults =
                    hybridRetriever.retrieve(
                            question.getQuestion(),
                            20
                    );

            List<String> rerankResults =
                    hybridRerankRetriever.retrieve(
                            question.getQuestion(),
                            20
                    );

            Integer hybridRank =
                    findBestRelevantRank(
                            question.getRelevantChunkIds(),
                            hybridResults
                    );

            Integer rerankRank =
                    findBestRelevantRank(
                            question.getRelevantChunkIds(),
                            rerankResults
                    );

            Integer improvement = null;

            if (hybridRank != null &&
                    rerankRank != null) {

                improvement =
                        hybridRank - rerankRank;
            }

            results.add(
                    RetrieverComparisonDto.builder()
                            .question(
                                    question.getQuestion()
                            )
                            .hybridRank(
                                    hybridRank
                            )
                            .hybridRerankRank(
                                    rerankRank
                            )
                            .improvement(
                                    improvement
                            )
                            .build()
            );
        }

        return results;
    }

    public CategorySummaryResponseDto
    getCategorySummary(
            EvaluationRetriever retriever,
            String retrieverName
    ) {

        List<EvaluationQuestion> dataset =
                datasetService.loadDataset();

        List<CategoryMetricsDto> results =
                new ArrayList<>();

        for (QuestionCategory category :
                QuestionCategory.values()) {

            List<EvaluationQuestion> categoryQuestions =
                    dataset.stream()
                            .filter(q ->
                                    q.getCategory() != null
                                            && q.getCategory() == category)
                            .toList();

            if (categoryQuestions.isEmpty()) {
                continue;
            }

            double totalRecall = 0;
            double totalPrecision = 0;
            double totalMrr = 0;
            double totalNdcg = 0;
            double totalMap = 0;
            double totalHitRate = 0;

            for (EvaluationQuestion question :
                    categoryQuestions) {

                List<String> retrieved =
                        retriever.retrieve(
                                question.getQuestion(),
                                20
                        );

                totalRecall +=
                        recallCalculator.calculate(
                                question.getRelevantChunkIds(),
                                retrieved
                        );

                totalPrecision +=
                        precisionCalculator.calculate(
                                question.getRelevantChunkIds(),
                                retrieved
                        );

                totalMrr +=
                        mrrCalculator.calculate(
                                question.getRelevantChunkIds(),
                                retrieved
                        );

                totalNdcg +=
                        ndcgCalculator.calculate(
                                question.getRelevantChunkIds(),
                                retrieved
                        );

                totalMap +=
                        mapCalculator.calculate(
                                question.getRelevantChunkIds(),
                                retrieved
                        );

                totalHitRate +=
                        hitRateCalculator.calculate(
                                question.getRelevantChunkIds(),
                                retrieved
                        );
            }

            int count =
                    categoryQuestions.size();

            results.add(
                    CategoryMetricsDto.builder()
                            .category(category)
                            .recall(totalRecall / count)
                            .precision(totalPrecision / count)
                            .mrr(totalMrr / count)
                            .ndcg(totalNdcg / count)
                            .map(totalMap / count)
                            .hitRate(totalHitRate / count)
                            .totalQuestions(count)
                            .build()
            );
        }

        return CategorySummaryResponseDto.builder()
                .retriever(retrieverName)
                .categories(results)
                .build();
    }

    public CategorySummaryResponseDto
    getCategorySummary(
            String retriever
    ) {

        return switch (retriever.toLowerCase()) {

            case "bm25" ->
                    getCategorySummary(
                            bm25Retriever,
                            "BM25"
                    );

            case "vector" ->
                    getCategorySummary(
                            vectorRetriever,
                            "VECTOR"
                    );

            case "hybrid" ->
                    getCategorySummary(
                            hybridRetriever,
                            "HYBRID"
                    );

            case "hybridrerank" ->
                    getCategorySummary(
                            hybridRerankRetriever,
                            "HYBRID_RERANK"
                    );

            default ->
                    throw new IllegalArgumentException(
                            "Unknown retriever: "
                                    + retriever
                    );
        };
    }

    private EvaluationRetriever getRetriever(
            EvaluationRetrieverType retrieverType
    ) {

        return switch (retrieverType) {

            case BM25 ->
                    bm25Retriever;

            case VECTOR ->
                    vectorRetriever;

            case HYBRID ->
                    hybridRetriever;

            case HYBRID_RERANK ->
                    hybridRerankRetriever;
        };
    }

    private CategoryMetricsDto  calculateCategoryMetrics(
            EvaluationRetrieverType retrieverType,
            QuestionCategory category
    ) {

        EvaluationRetriever retriever =
                getRetriever(retrieverType);

        List<EvaluationQuestion> dataset =
                datasetService.loadDataset();

        List<EvaluationQuestion> categoryQuestions =
                dataset.stream()
                        .filter(q ->
                                q.getCategory() == category)
                        .toList();

        if (categoryQuestions.isEmpty()) {

            return CategoryMetricsDto.builder()
                    .category(category)
                    .recall(0)
                    .precision(0)
                    .mrr(0)
                    .ndcg(0)
                    .map(0)
                    .hitRate(0)
                    .totalQuestions(0)
                    .build();
        }

        double totalRecall = 0;
        double totalPrecision = 0;
        double totalMrr = 0;
        double totalNdcg = 0;
        double totalMap = 0;
        double totalHitRate = 0;

        for (EvaluationQuestion question :
                categoryQuestions) {

            List<String> retrieved =
                    retriever.retrieve(
                            question.getQuestion(),
                            RETRIEVAL_K
                    );

            totalRecall +=
                    recallCalculator.calculate(
                            question.getRelevantChunkIds(),
                            retrieved
                    );

            totalPrecision +=
                    precisionCalculator.calculate(
                            question.getRelevantChunkIds(),
                            retrieved
                    );

            totalMrr +=
                    mrrCalculator.calculate(
                            question.getRelevantChunkIds(),
                            retrieved
                    );

            totalNdcg +=
                    ndcgCalculator.calculate(
                            question.getRelevantChunkIds(),
                            retrieved
                    );

            totalMap +=
                    mapCalculator.calculate(
                            question.getRelevantChunkIds(),
                            retrieved
                    );

            totalHitRate +=
                    hitRateCalculator.calculate(
                            question.getRelevantChunkIds(),
                            retrieved
                    );
        }

        int count =
                categoryQuestions.size();

        return CategoryMetricsDto.builder()
                .category(category)
                .recall(totalRecall / count)
                .precision(totalPrecision / count)
                .mrr(totalMrr / count)
                .ndcg(totalNdcg / count)
                .map(totalMap / count)
                .hitRate(totalHitRate / count)
                .totalQuestions(count)
                .build();
    }

    public List<CategoryComparisonResponse> compareCategories() {

        List<CategoryComparisonResponse> result =
                new ArrayList<>();

        for (QuestionCategory category :
                QuestionCategory.values()) {

            CategoryMetricsDto hybrid =
                    calculateCategoryMetrics(
                            EvaluationRetrieverType.HYBRID,
                            category
                    );

            CategoryMetricsDto rerank =
                    calculateCategoryMetrics(
                            EvaluationRetrieverType.HYBRID_RERANK,
                            category
                    );

            result.add(
                    CategoryComparisonResponse.builder()
                            .category(category)

                            .hybridRecall(
                                    hybrid.getRecall()
                            )
                            .rerankRecall(
                                    rerank.getRecall()
                            )
                            .recallImprovement(
                                    rerank.getRecall()
                                            - hybrid.getRecall()
                            )

                            .hybridMrr(
                                    hybrid.getMrr()
                            )
                            .rerankMrr(
                                    rerank.getMrr()
                            )
                            .mrrImprovement(
                                    rerank.getMrr()
                                            - hybrid.getMrr()
                            )

                            .hybridNdcg(
                                    hybrid.getNdcg()
                            )
                            .rerankNdcg(
                                    rerank.getNdcg()
                            )
                            .ndcgImprovement(
                                    rerank.getNdcg()
                                            - hybrid.getNdcg()
                            )

                            .hybridMap(
                                    hybrid.getMap()
                            )
                            .rerankMap(
                                    rerank.getMap()
                            )
                            .mapImprovement(
                                    rerank.getMap()
                                            - hybrid.getMap()
                            )
                            .build()
            );
        }

        return result;
    }

    public List<CategoryFailureDto> getCategoryFailures() {

        List<EvaluationQuestion> dataset =
                datasetService.loadDataset();

        List<CategoryFailureDto> results =
                new ArrayList<>();

        for (QuestionCategory category :
                QuestionCategory.values()) {

            if (category == QuestionCategory.UNKNOWN) {
                continue;
            }

            List<EvaluationQuestion> categoryQuestions =
                    dataset.stream()
                            .filter(q ->
                                    q.getCategory() == category)
                            .toList();

            if (categoryQuestions.isEmpty()) {
                continue;
            }

            long success = 0;
            long lowRank = 0;
            long partialMatch = 0;
            long miss = 0;

            for (EvaluationQuestion question :
                    categoryQuestions) {

                List<String> retrieved =
                        hybridRetriever.retrieve(
                                question.getQuestion(),
                                RETRIEVAL_K
                        );

                FailureType failureType =
                        determineFailureType(
                                question.getRelevantChunkIds(),
                                retrieved
                        );

                switch (failureType) {

                    case SUCCESS -> success++;

                    case LOW_RANK -> lowRank++;

                    case PARTIAL_MATCH -> partialMatch++;

                    case MISS -> miss++;
                }
            }

            results.add(
                    CategoryFailureDto.builder()
                            .category(category)
                            .success(success)
                            .lowRank(lowRank)
                            .partialMatch(partialMatch)
                            .miss(miss)
                            .totalQuestions(
                                    categoryQuestions.size()
                            )
                            .build()
            );
        }

        return results;

    }

    public List<CategoryFailureDetailDto>
    getCategoryDetails(
            QuestionCategory category
    ) {

        List<EvaluationQuestion> dataset =
                datasetService.loadDataset();

        List<CategoryFailureDetailDto> results =
                new ArrayList<>();

        List<EvaluationQuestion> categoryQuestions =
                dataset.stream()
                        .filter(q ->
                                q.getCategory() == category)
                        .toList();

        for (EvaluationQuestion question :
                categoryQuestions) {

            List<String> retrieved =
                    hybridRetriever.retrieve(
                            question.getQuestion(),
                            RETRIEVAL_K
                    );

            FailureType failureType =
                    determineFailureType(
                            question.getRelevantChunkIds(),
                            retrieved
                    );

            if (failureType == FailureType.SUCCESS) {
                continue;
            }

            results.add(
                    CategoryFailureDetailDto.builder()
                            .question(
                                    question.getQuestion()
                            )
                            .expectedChunkIds(
                                    question.getRelevantChunkIds()
                            )
                            .retrievedChunkIds(
                                    retrieved
                            )
                            .bestRelevantRank(
                                    findBestRelevantRank(
                                            question.getRelevantChunkIds(),
                                            retrieved
                                    )
                            )
                            .failureType(
                                    failureType
                            )
                            .build()
            );
        }

        results.sort(
                Comparator.comparing(
                        CategoryFailureDetailDto::getBestRelevantRank,
                        Comparator.nullsLast(
                                Comparator.reverseOrder()
                        )
                )
        );

        return results;
    }

}