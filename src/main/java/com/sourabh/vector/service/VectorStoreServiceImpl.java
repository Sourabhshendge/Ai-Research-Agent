package com.sourabh.vector.service;


import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VectorStoreServiceImpl implements VectorStoreService {

    private final VectorStore vectorStore;

    @Override
    public void upsertChunk(
            String vectorId,
            String chunkText,
            Long documentId,
            String fileName,
            Integer chunkIndex,
            List<Float> embedding
    ) {

        Document document = new Document(
                chunkText,
                Map.of(
                        "vectorId", vectorId,
                        "documentId", String.valueOf(documentId),
                        "fileName", fileName,
                        "chunkIndex", String.valueOf(chunkIndex)
                )
        );

        vectorStore.add(List.of(document));
    }
}