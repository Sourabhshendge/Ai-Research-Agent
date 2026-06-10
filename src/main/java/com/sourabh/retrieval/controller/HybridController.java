package com.sourabh.retrieval.controller;

import com.sourabh.common.response.ApiResponse;
import com.sourabh.retrieval.dto.HybridResultDto;
import com.sourabh.retrieval.service.HybridSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hybrid")
public class HybridController {

    private final HybridSearchService hybridSearchService;

    @PostMapping("/evaluate")
    public ApiResponse<List<HybridResultDto>>
    evaluate(
            @RequestParam String query
    ) {

        return ApiResponse.success(
                hybridSearchService.search(
                        query,
                        20
                )
        );
    }
}