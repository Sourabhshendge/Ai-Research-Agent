package com.sourabh.rerank.service;

import com.sourabh.retrieval.dto.HybridResultDto;
import com.sourabh.retrieval.service.HybridSearchService;
import com.sourabh.rerank.dto.RerankCandidate;
import com.sourabh.rerank.dto.RerankResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RerankEvaluationService {

    private final HybridSearchService hybridSearchService;
    private final RerankerService rerankerService;

    public List<RerankResponseDto> evaluate(
            String query
    ) {

        List<HybridResultDto> hybridResults =
                hybridSearchService.search(
                        query,
                        20
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
                        5
                );

        return reranked.stream()
                .map(candidate ->
                        RerankResponseDto.builder()
                                .content(candidate.content())
                                .fileName(candidate.fileName())
                                .documentId(candidate.documentId())
                                .chunkIndex(candidate.chunkIndex())
                                .retrievalScore(
                                        candidate.retrievalScore()
                                )
                                .build()
                )
                .toList();
    }
}