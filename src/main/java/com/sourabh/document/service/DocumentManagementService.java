package com.sourabh.document.service;

import com.sourabh.document.dto.DocumentStatsResponse;
import com.sourabh.document.entity.Document;

public interface DocumentManagementService {

    void deleteDocument(Long documentId);

    DocumentStatsResponse getStats();

    void reindexDocument(Long documentId);

    interface DocumentProcessingService {

        void processDocument(
                Document document
        );

    }
}