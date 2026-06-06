package com.sourabh.embedding.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OllamaEmbeddingProvider implements EmbeddingProvider {

    private final EmbeddingModel embeddingModel;

    @Override
    public List<Float> generateEmbedding(String text) {

        float[] embedding = embeddingModel.embed(text);

        List<Float> result = new ArrayList<>(embedding.length);

        for (float value : embedding) {
            result.add(value);
        }

        return result;
    }
}