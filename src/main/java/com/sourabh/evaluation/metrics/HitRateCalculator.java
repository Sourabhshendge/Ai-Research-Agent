package com.sourabh.evaluation.metrics;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class HitRateCalculator {

    public double calculate(
            List<String> relevantChunkIds,
            List<String> retrievedChunkIds
    ) {

        Set<String> relevant =
                Set.copyOf(relevantChunkIds);

        boolean hit =
                retrievedChunkIds.stream()
                        .anyMatch(relevant::contains);

        return hit ? 1.0 : 0.0;
    }
}