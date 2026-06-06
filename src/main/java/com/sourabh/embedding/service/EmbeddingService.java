package com.sourabh.embedding.service;

import com.sourabh.embedding.provider.EmbeddingProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmbeddingService {

    private final EmbeddingProvider embeddingProvider;

    public List<Float> generateEmbedding(String text) {

        return embeddingProvider.generateEmbedding(text);
    }
}