package com.sourabh.rerank.controller;

import com.sourabh.common.response.ApiResponse;
import com.sourabh.rerank.dto.RerankRequest;
import com.sourabh.rerank.dto.RerankResponseDto;
import com.sourabh.rerank.service.RerankEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rerank")
public class RerankController {

    private final RerankEvaluationService rerankEvaluationService;

    @PostMapping("/evaluate")
    public ApiResponse<List<RerankResponseDto>> evaluate(
            @RequestBody RerankRequest request
    ) {

        return ApiResponse.success(
                rerankEvaluationService.evaluate(
                        request.getQuery()
                )
        );
    }
}