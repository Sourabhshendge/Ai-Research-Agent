package com.sourabh.evaluation.retriever;

import com.sourabh.search.document.DocumentChunkIndex;
import com.sourabh.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("bm25Retriever")
@RequiredArgsConstructor
public class Bm25EvaluationRetriever
        implements EvaluationRetriever {

    private final SearchService searchService;

    @Override
    public List<String> retrieve(
            String query,
            int topK
    ) {

        List<DocumentChunkIndex> results =
                searchService.searchChunks(
                        query,
                        topK
                );

        System.out.println(
                "BM25 Results = " + results.size()
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