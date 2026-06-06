package com.sourabh.retrieval.controller;

import com.sourabh.common.response.ApiResponse;
import com.sourabh.retrieval.dto.RetrievalEvaluationRequest;
import com.sourabh.retrieval.dto.RetrievalEvaluationResponse;
import com.sourabh.retrieval.service.RetrievalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/retrieval")
public class RetrievalController {

    private final RetrievalService retrievalService;

    @PostMapping("/evaluate")
    public ApiResponse<RetrievalEvaluationResponse> evaluate(
            @RequestBody RetrievalEvaluationRequest request
    ) {

        return ApiResponse.success(
                retrievalService.evaluate(
                        request.getQuestion()
                )
        );
    }
}
