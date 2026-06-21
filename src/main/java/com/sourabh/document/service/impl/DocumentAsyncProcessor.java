package com.sourabh.document.service.impl;

import com.sourabh.common.exception.ResourceNotFoundException;
import com.sourabh.document.dto.ChunkDto;
import com.sourabh.document.entity.Document;
import com.sourabh.document.entity.DocumentChunk;
import com.sourabh.document.entity.DocumentStatus;
import com.sourabh.document.repository.DocumentChunkRepository;
import com.sourabh.document.repository.DocumentRepository;
import com.sourabh.document.service.TextExtractionService;
import com.sourabh.embedding.service.EmbeddingService;
import com.sourabh.search.service.DocumentIndexingServiceImpl;
import com.sourabh.vector.service.VectorStoreServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.List;

import static org.springframework.ai.util.json.JsonParser.toJson;

@Service
@RequiredArgsConstructor
public class DocumentAsyncProcessor {

    private final ChunkingServiceImpl chunkingService;
    private final EmbeddingService embeddingService;
    private final DocumentChunkRepository documentChunkRepository;
    private final DocumentIndexingServiceImpl indexingService;
    private final VectorStoreServiceImpl vectorStoreService;
    private final DocumentRepository documentRepository;
    private final TextExtractionService textExtractionService;

    @Transactional
    public void processDocument(
            Long documentId
    ) {

        Document document =
                documentRepository
                        .findById(documentId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Document not found with id: "
                                                + documentId
                                )
                        );

        try {

            document.setStatus(
                    DocumentStatus.PROCESSING
            );

            documentRepository.save(document);

            String extractedText =
                    textExtractionService.extract(
                            Paths.get(
                                    document.getFilePath()
                            )
                    );

            document.setExtractedText(
                    extractedText
            );

            documentRepository.save(
                    document
            );

            List<ChunkDto> chunks =
                    chunkingService.chunk(
                            extractedText
                    );

            for (ChunkDto chunk : chunks) {

                List<Float> embedding =
                        embeddingService.generateEmbedding(
                                chunk.chunkText()
                        );

                String vectorId =
                        document.getId()
                                + "_"
                                + chunk.chunkIndex();

                DocumentChunk savedChunk =
                        documentChunkRepository.save(
                                DocumentChunk.builder()
                                        .document(document)
                                        .chunkIndex(
                                                chunk.chunkIndex()
                                        )
                                        .chunkText(
                                                chunk.chunkText()
                                        )
                                        .embedding(
                                                toJson(embedding)
                                        )
                                        .vectorId(vectorId)
                                        .build()
                        );

                indexingService.indexChunk(
                        savedChunk
                );

                vectorStoreService.upsertChunk(
                        vectorId,
                        chunk.chunkText(),
                        document.getId(),
                        document.getFileName(),
                        chunk.chunkIndex(),
                        embedding
                );
            }

            document.setStatus(
                    DocumentStatus.READY
            );

            document.setFailureReason(
                    null
            );

            documentRepository.save(
                    document
            );

        } catch (Exception ex) {

            document.setStatus(
                    DocumentStatus.FAILED
            );

            document.setFailureReason(
                    ex.getMessage()
            );

            documentRepository.save(
                    document
            );

            throw ex;
        }
    }

}