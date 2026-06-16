package com.sourabh.evaluation.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourabh.evaluation.dto.EvaluationQuestion;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationDatasetService {

    private final ObjectMapper objectMapper;

    public List<EvaluationQuestion> loadDataset() {

        try {

            ClassPathResource resource =
                    new ClassPathResource(
                            "evaluation/evaluation-dataset.json"
                    );

            System.out.println("Exists = " + resource.exists());

            InputStream inputStream = resource.getInputStream();

            return objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<EvaluationQuestion>>() {
                    });

        } catch (Exception ex) {
            throw new RuntimeException(
                    "Failed to load evaluation dataset",
                    ex
            );
        }
    }
}