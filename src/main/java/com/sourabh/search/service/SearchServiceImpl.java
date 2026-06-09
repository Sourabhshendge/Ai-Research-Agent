package com.sourabh.search.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import com.sourabh.retrieval.dto.SearchResultDto;
import com.sourabh.search.document.DocumentChunkIndex;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<SearchResultDto> search(String query) {

        NativeQuery searchQuery =
                NativeQuery.builder()
                        .withQuery(q ->
                                q.match(m ->
                                        m.field("content")
                                                .query(query)
                                                .operator(Operator.And)
                                )
                        )
                        .withMaxResults(5)
                        .build();

        return elasticsearchOperations
                .search(searchQuery, DocumentChunkIndex.class)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<DocumentChunkIndex> searchChunks(
            String query,
            int size
    ) {

        NativeQuery nativeQuery =
                NativeQuery.builder()
                        .withQuery(q ->
                                q.match(m ->
                                        m.field("content")
                                                .query(query)
                                                .operator(Operator.And)
                                )
                        )
                        .withMaxResults(size)
                        .build();

        SearchHits<DocumentChunkIndex> hits =
                elasticsearchOperations.search(
                        nativeQuery,
                        DocumentChunkIndex.class
                );

        return hits.getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .toList();
    }

    private SearchResultDto mapToDto(
            SearchHit<DocumentChunkIndex> hit
    ) {

        DocumentChunkIndex doc = hit.getContent();

        return SearchResultDto.builder()
                .documentId(doc.getDocumentId())
                .chunkIndex(doc.getChunkIndex())
                .fileName(doc.getFileName())
                .content(doc.getContent())
                .score(Double.valueOf(hit.getScore()))
                .build();
    }
}