package com.sourabh.evaluation.metrics;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RecallCalculator {

    public double calculate(
            List<String> relevantChunkIds,
            List<String> retrievedChunkIds
    ) {

        if (relevantChunkIds == null ||
                relevantChunkIds.isEmpty()) {
            return 0.0;
        }

        Set<String> retrievedSet =
                retrievedChunkIds.stream()
                        .collect(Collectors.toSet());

        long relevantRetrieved =
                relevantChunkIds.stream()
                        .filter(retrievedSet::contains)
                        .count();

        return (double) relevantRetrieved
                / relevantChunkIds.size();
    }
}