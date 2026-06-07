package com.sourabh.search.service;

import com.sourabh.document.entity.DocumentChunk;

import java.util.List;

public interface DocumentIndexingService {

    void indexChunk(DocumentChunk chunk);

    void indexChunks(List<DocumentChunk> chunks);

    void reindexAll();
}