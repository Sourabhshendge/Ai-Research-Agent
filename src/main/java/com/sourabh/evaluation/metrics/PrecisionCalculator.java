package com.sourabh.evaluation.metrics;


import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PrecisionCalculator {

    public double calculate(
            List<String> relevantChunkIds,
            List<String> retrievedChunkIds
    ) {

        if (retrievedChunkIds == null ||
                retrievedChunkIds.isEmpty()) {
            return 0.0;
        }

        Set<String> relevantSet =
                relevantChunkIds.stream()
                        .collect(Collectors.toSet());

        long relevantRetrieved =
                retrievedChunkIds.stream()
                        .filter(relevantSet::contains)
                        .count();

        return (double) relevantRetrieved
                / retrievedChunkIds.size();
    }
}