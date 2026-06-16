package com.sourabh.evaluation.retriever;

import com.sourabh.retrieval.dto.HybridResultDto;
import com.sourabh.retrieval.service.HybridSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("hybridRetriever")
@RequiredArgsConstructor
public class HybridEvaluationRetriever
        implements EvaluationRetriever {

    private final HybridSearchService hybridSearchService;

    @Override
    public List<String> retrieve(
            String query,
            int topK
    ) {

        List<HybridResultDto> results =
                hybridSearchService.search(
                        query,
                        topK
                );

        return results.stream()
                .map(result ->
                        result.documentId()
                                + "_"
                                + result.chunkIndex()
                )
                .toList();
    }
}