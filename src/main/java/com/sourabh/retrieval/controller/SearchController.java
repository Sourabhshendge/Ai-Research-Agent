package com.sourabh.retrieval.controller;

import com.sourabh.common.response.ApiResponse;
import com.sourabh.retrieval.dto.SearchResultDto;
import com.sourabh.retrieval.service.RetrievalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final RetrievalService retrievalService;

    @GetMapping
    public ApiResponse<List<SearchResultDto>> search(
            @RequestParam String query
    ) {

        return ApiResponse.success(
                retrievalService.search(query, 5)
        );
    }
}