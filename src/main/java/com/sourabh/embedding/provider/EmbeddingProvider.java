package com.sourabh.embedding.provider;

import java.util.List;

public interface EmbeddingProvider {

    List<Float> generateEmbedding(String text);
}