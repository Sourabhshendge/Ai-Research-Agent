package com.sourabh.search.service;

import com.sourabh.retrieval.dto.SearchResultDto;
import com.sourabh.search.document.DocumentChunkIndex;

import java.util.List;

public interface SearchService {
    List<SearchResultDto> search(
            String query
    );

    List<DocumentChunkIndex> searchChunks(
            String query,
            int size
    );
}

