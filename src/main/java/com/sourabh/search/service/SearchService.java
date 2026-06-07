package com.sourabh.search.service;

import com.sourabh.retrieval.dto.SearchResultDto;

import java.util.List;

public interface SearchService {
    List<SearchResultDto> search(
            String query
    );
}
