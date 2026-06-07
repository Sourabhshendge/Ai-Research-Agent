package com.sourabh.search.controller;


import com.sourabh.common.response.ApiResponse;
import com.sourabh.search.dto.SearchRequest;
import com.sourabh.search.dto.SearchResponse;
import com.sourabh.search.service.SearchService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class ElasticsearchSearchController   {

    private final SearchService searchService;

    @PostMapping("/evaluate")
    public ApiResponse<SearchResponse> evaluate(
            @Valid @RequestBody SearchRequest request
    ) {

        return ApiResponse.success(
                SearchResponse.builder()
                        .results(
                                searchService.search(
                                        request.getQuery()
                                )
                        )
                        .build()
        );
    }
}