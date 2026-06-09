package com.sourabh.retrieval.service;

import com.sourabh.retrieval.dto.RetrievalEvaluationResponse;
import com.sourabh.retrieval.dto.SearchResultDto;

import java.util.List;

public interface RetrievalService {

    List<SearchResultDto> search(
            String query,
            int topK
    );

    RetrievalEvaluationResponse evaluate(
            String question
    );

    List<SearchResultDto> retrieve(
            String query,
            int topK
    );
}