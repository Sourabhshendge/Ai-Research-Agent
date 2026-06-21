package com.sourabh.vector.service;

import java.util.List;

public interface VectorStoreService {

    void upsertChunk(
            String vectorId,
            String chunkText,
            Long documentId,
            String fileName,
            Integer chunkIndex,
            List<Float> embedding
    );

    boolean isHealthy();
}
