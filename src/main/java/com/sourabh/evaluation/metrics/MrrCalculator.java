package com.sourabh.evaluation.metrics;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class MrrCalculator {

    public double calculate(
            List<String> relevantChunkIds,
            List<String> retrievedChunkIds
    ) {

        Set<String> relevantSet =
                Set.copyOf(relevantChunkIds);

        for (int i = 0; i < retrievedChunkIds.size(); i++) {

            if (relevantSet.contains(
                    retrievedChunkIds.get(i))) {

                return 1.0 / (i + 1);
            }
        }

        return 0.0;
    }
}