package com.sourabh.search.dto;

import com.sourabh.retrieval.dto.SearchResultDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchResponse {

    private List<SearchResultDto> results;
}