package com.sourabh.rerank.service;

import com.sourabh.rerank.dto.RerankCandidate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
    public class SimpleRerankerService
        implements RerankerService {

    @Override
    public List<RerankCandidate> rerank(
            String query,
            List<RerankCandidate> candidates,
            int topK
    ) {

        return candidates.stream()
                .limit(topK)
                .toList();
    }
}