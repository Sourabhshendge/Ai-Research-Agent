package com.sourabh.evaluation.retriever;

import com.sourabh.retrieval.dto.HybridResultDto;
import com.sourabh.retrieval.service.HybridSearchService;
import com.sourabh.rerank.dto.RerankCandidate;
import com.sourabh.rerank.service.RerankerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("hybridRerankRetriever")
@RequiredArgsConstructor
public class HybridRerankEvaluationRetriever
        implements EvaluationRetriever {

    private final HybridSearchService hybridSearchService;
    private final RerankerService rerankerService;

    @Override
    public List<String> retrieve(
            String query,
            int topK
    ) {

        int candidatePool =
                Math.max(topK * 4, 20);

        List<HybridResultDto> hybridResults =
                hybridSearchService.search(
                        query,
                        candidatePool
                );

        List<RerankCandidate> candidates =
                hybridResults.stream()
                        .map(result ->
                                RerankCandidate.builder()
                                        .content(result.content())
                                        .fileName(result.documentName())
                                        .documentId(result.documentId())
                                        .chunkIndex(result.chunkIndex())
                                        .retrievalScore(result.score())
                                        .build()
                        )
                        .toList();

        List<RerankCandidate> reranked =
                rerankerService.rerank(
                        query,
                        candidates,
                        topK
                );

        return reranked.stream()
                .map(candidate ->
                        candidate.documentId()
                                + "_"
                                + candidate.chunkIndex()
                )
                .toList();
    }
}