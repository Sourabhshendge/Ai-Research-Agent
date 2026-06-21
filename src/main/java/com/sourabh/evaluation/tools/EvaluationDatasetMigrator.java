package com.sourabh.evaluation.tools;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourabh.evaluation.dto.EvaluationQuestion;

import java.io.File;
import java.util.List;
import java.util.Map;

public class EvaluationDatasetMigrator {

    public static void main(String[] args) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        File inputFile = new File(
                "src/main/resources/evaluation/evaluation-dataset.json"
        );

        List<EvaluationQuestion> questions =
                mapper.readValue(
                        inputFile,
                        new TypeReference<List<EvaluationQuestion>>() {}
                );

        Map<String, String> prefixMap = Map.of(
                "3_", "1_",
                "4_", "2_",
                "5_", "3_"
        );

        for (EvaluationQuestion question : questions) {

            List<String> migratedIds =
                    question.getRelevantChunkIds()
                            .stream()
                            .map(id -> migrateChunkId(id, prefixMap))
                            .toList();

            question.setRelevantChunkIds(migratedIds);
        }

        File outputFile = new File(
                "src/main/resources/evaluation/evaluation-dataset-migrated.json"
        );

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(outputFile, questions);

        System.out.println("Migration completed.");
        System.out.println("Output: " + outputFile.getAbsolutePath());
    }

    private static String migrateChunkId(
            String chunkId,
            Map<String, String> prefixMap
    ) {

        for (Map.Entry<String, String> entry : prefixMap.entrySet()) {

            if (chunkId.startsWith(entry.getKey())) {

                return chunkId.replaceFirst(
                        entry.getKey(),
                        entry.getValue()
                );
            }
        }

        return chunkId;
    }
}