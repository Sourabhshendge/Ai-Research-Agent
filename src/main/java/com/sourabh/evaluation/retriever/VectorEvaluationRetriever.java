package com.sourabh.evaluation.retriever;

import com.sourabh.retrieval.dto.SearchResultDto;
import com.sourabh.retrieval.service.RetrievalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("vectorRetriever")
@RequiredArgsConstructor
public class VectorEvaluationRetriever
        implements EvaluationRetriever {

    private final RetrievalService retrievalService;

    @Override
    public List<String> retrieve(
            String query,
            int topK
    ) {

        List<SearchResultDto> results =
                retrievalService.retrieve(
                        query,
                        topK
                );

        return results.stream()
                .map(result ->
                        result.getDocumentId()
                                + "_"
                                + result.getChunkIndex()
                )
                .toList();
    }
}