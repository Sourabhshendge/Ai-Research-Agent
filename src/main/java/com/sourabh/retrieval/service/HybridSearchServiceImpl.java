package com.sourabh.retrieval.service;

import com.sourabh.retrieval.dto.HybridResultDto;
import com.sourabh.retrieval.dto.RrfCandidate;
import com.sourabh.retrieval.dto.SearchResultDto;
import com.sourabh.search.document.DocumentChunkIndex;
import com.sourabh.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HybridSearchServiceImpl
        implements HybridSearchService {

    private static final int RRF_K = 60;

    private final SearchService searchService;
    private final RetrievalService retrievalService;

    @Override
    public List<HybridResultDto> search(
            String query,
            int topK
    ) {

        int candidatePool = Math.max(topK * 4, 20);

        List<DocumentChunkIndex> bm25Results =
                searchService.searchChunks(
                        query,
                        candidatePool
                );

        List<SearchResultDto> vectorResults =
                retrievalService.retrieve(
                        query,
                        candidatePool
                );


        Map<String, RrfCandidate> fusedResults =
                new HashMap<>();

        for (int rank = 0; rank < bm25Results.size(); rank++) {

            DocumentChunkIndex chunk =
                    bm25Results.get(rank);

            String fusionKey =
                    buildFusionKey(
                            chunk.getDocumentId(),
                            chunk.getChunkIndex()
                    );

            double rrfScore =
                    1.0 / (RRF_K + rank + 1);

            fusedResults.compute(
                    fusionKey,
                    (key, existing) -> {

                        if (existing == null) {
                            return new RrfCandidate(
                                    fusionKey,
                                    chunk.getContent(),
                                    chunk.getFileName(),
                                    chunk.getDocumentId(),
                                    chunk.getChunkIndex(),
                                    rrfScore
                            );
                        }

                        return new RrfCandidate(
                                existing.chunkId(),
                                existing.content(),
                                existing.documentName(),
                                existing.documentId(),
                                existing.chunkIndex(),
                                existing.score() + rrfScore
                        );
                    }
            );
        }

        for (int rank = 0; rank < vectorResults.size(); rank++) {

            SearchResultDto result =
                    vectorResults.get(rank);

            String fusionKey =
                    buildFusionKey(
                            result.getDocumentId(),
                            result.getChunkIndex()
                    );

            double rrfScore =
                    1.0 / (RRF_K + rank + 1);

            fusedResults.compute(
                    fusionKey,
                    (key, existing) -> {

                        if (existing == null) {
                            return new RrfCandidate(
                                    fusionKey,
                                    result.getContent(),
                                    result.getFileName(),
                                    result.getDocumentId(),
                                    result.getChunkIndex(),
                                    rrfScore
                            );
                        }

                        return new RrfCandidate(
                                existing.chunkId(),
                                existing.content(),
                                existing.documentName(),
                                existing.documentId(),
                                existing.chunkIndex(),
                                existing.score() + rrfScore
                        );
                    }
            );
        }





        return fusedResults.values()
                .stream()
                .sorted(
                        Comparator.comparingDouble(
                                RrfCandidate::score
                        ).reversed()
                )
                .limit(topK)
                .map(candidate ->
                        new HybridResultDto(
                                candidate.chunkId(),
                                candidate.content(),
                                candidate.documentName(),
                                candidate.documentId(),
                                candidate.chunkIndex(),
                                candidate.score()
                        )
                )
                .toList();
    }

    private String buildFusionKey(
            Long documentId,
            Integer chunkIndex
    ) {
        return documentId + "_" + chunkIndex;
    }
}
