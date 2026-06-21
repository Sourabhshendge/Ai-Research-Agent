package com.sourabh.evaluation.metrics;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MapCalculator {

    public double calculate(
            List<String> relevantChunkIds,
            List<String> retrievedChunkIds
    ) {

        if (relevantChunkIds.isEmpty()) {
            return 0.0;
        }

        Set<String> relevant =
                new HashSet<>(relevantChunkIds);

        int relevantFound = 0;

        double precisionSum = 0.0;

        for (int i = 0; i < retrievedChunkIds.size(); i++) {

            String retrieved =
                    retrievedChunkIds.get(i);

            if (relevant.contains(retrieved)) {

                relevantFound++;

                double precisionAtRank =
                        (double) relevantFound
                                / (i + 1);

                precisionSum += precisionAtRank;
            }
        }

        return precisionSum
                / relevantChunkIds.size();
    }
}