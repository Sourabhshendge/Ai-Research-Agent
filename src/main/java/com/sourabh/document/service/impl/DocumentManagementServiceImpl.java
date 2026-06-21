package com.sourabh.document.service.impl;

import com.sourabh.document.dto.DocumentStatsResponse;
import com.sourabh.document.entity.Document;
import com.sourabh.document.repository.DocumentChunkRepository;
import com.sourabh.document.repository.DocumentRepository;
import com.sourabh.document.service.DocumentManagementService;
import com.sourabh.document.service.FileStorageService;
import com.sourabh.search.service.DocumentIndexingService;
import com.sourabh.vector.service.QdrantCleanupService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentManagementServiceImpl
        implements DocumentManagementService {

    private final DocumentRepository repository;
    private final QdrantCleanupService qdrantCleanupService;

    private final DocumentChunkRepository
            documentChunkRepository;

    private final FileStorageService
            fileStorageService;

    private final DocumentIndexingService
            indexingService;

    private final DocumentAsyncProcessor
            documentAsyncProcessor;


    @Override
    public void deleteDocument(
            Long documentId
    ) {

        Document document =
                repository.findById(
                                documentId
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Document not found"
                                )
                        );

        qdrantCleanupService.deleteDocumentVectors(
                documentId
        );

        /*
         * Elasticsearch cleanup
         */
        indexingService.deleteDocumentChunks(
                documentId
        );

        /*
         * MySQL chunks
         */
        documentChunkRepository
                .deleteByDocument_Id(
                        documentId
                );

        /*
         * Physical file
         */
        fileStorageService.delete(
                document.getFilePath()
        );

        /*
         * Document row
         */
        repository.delete(
                document
        );
    }

    @Override
    public DocumentStatsResponse getStats() {

        long totalDocuments =
                repository.count();

        long totalChunks =
                documentChunkRepository.getTotalChunkCount();

        long totalStorage =
                repository.getTotalStorageBytes();

        double avgChunks =
                totalDocuments == 0
                        ? 0
                        : (double) totalChunks / totalDocuments;

        return DocumentStatsResponse.builder()
                .totalDocuments(totalDocuments)
                .totalChunks(totalChunks)
                .averageChunksPerDocument(avgChunks)
                .totalStorageBytes(totalStorage)
                .build();
    }

    @Override
    @Transactional
    public void reindexDocument(
            Long documentId
    ) {

        Document document =
                repository.findById(
                                documentId
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Document not found"
                                )
                        );

        /*
         * Remove old chunks
         */
        indexingService.deleteDocumentChunks(
                documentId
        );

        documentChunkRepository
                .deleteByDocument_Id(
                        documentId
                );

        /*
         * Rebuild chunks
         */

                documentAsyncProcessor.processDocument(document.getId());
    }
}