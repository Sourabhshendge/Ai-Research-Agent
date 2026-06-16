package com.sourabh.evaluation.metrics;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class NdcgCalculator {

    public double calculate(
            List<String> relevantChunkIds,
            List<String> retrievedChunkIds
    ) {

        Set<String> relevantSet =
                Set.copyOf(relevantChunkIds);

        double dcg = 0.0;

        for (int i = 0; i < retrievedChunkIds.size(); i++) {

            if (relevantSet.contains(
                    retrievedChunkIds.get(i))) {

                dcg += 1.0 /
                        log2(i + 2);
            }
        }

        double idcg = 0.0;

        int idealRelevantCount =
                Math.min(
                        relevantChunkIds.size(),
                        retrievedChunkIds.size()
                );

        for (int i = 0; i < idealRelevantCount; i++) {

            idcg += 1.0 /
                    log2(i + 2);
        }

        if (idcg == 0.0) {
            return 0.0;
        }

        return dcg / idcg;
    }

    private double log2(int value) {
        return Math.log(value) /
                Math.log(2);
    }
}