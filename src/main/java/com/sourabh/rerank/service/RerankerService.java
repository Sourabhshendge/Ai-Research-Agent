package com.sourabh.rerank.service;

import com.sourabh.rerank.dto.RerankCandidate;

import java.util.List;

public interface RerankerService {

    List<RerankCandidate> rerank(
            String query,
            List<RerankCandidate> candidates,
            int topK
    );
}
