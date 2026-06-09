package com.sourabh.retrieval.service;

import com.sourabh.retrieval.dto.HybridResultDto;

import java.util.List;

public interface HybridSearchService {

    List<HybridResultDto> search(
            String query,
            int topK
    );
}