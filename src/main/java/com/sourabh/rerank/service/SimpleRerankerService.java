package com.sourabh.rerank.service;

import com.sourabh.rerank.dto.CrossEncoderInput;
import com.sourabh.rerank.dto.RerankCandidate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SimpleRerankerService
        implements RerankerService {

    private final CrossEncoderTokenizerService tokenizerService;

    private final CrossEncoderInferenceService inferenceService;

    @Override
    public List<RerankCandidate> rerank(
            String query,
            List<RerankCandidate> candidates,
            int topK
    ) {

        System.out.println(
                "===== CROSS ENCODER RERANKER CALLED ====="
        );

        return candidates.stream()
                .map(candidate -> {

                    try {

                        CrossEncoderInput input =
                                tokenizerService.tokenize(
                                        query,
                                        candidate.content()
                                );

                        double score =
                                inferenceService.score(input);

                        return RerankCandidate.builder()
                                .content(candidate.content())
                                .fileName(candidate.fileName())
                                .documentId(candidate.documentId())
                                .chunkIndex(candidate.chunkIndex())
                                .retrievalScore(
                                        candidate.retrievalScore()
                                )
                                .rerankScore(score)
                                .build();

                    } catch (Exception e) {

                        throw new RuntimeException(e);
                    }
                })
                .sorted(
                        (a, b) -> Double.compare(
                                b.rerankScore(),
                                a.rerankScore()
                        )
                )
                .limit(topK)
                .toList();
    }
}