package com.sourabh.document.service.impl;

import com.sourabh.document.dto.ChunkDto;
import com.sourabh.document.dto.DocumentResponse;
import com.sourabh.document.entity.Document;
import com.sourabh.document.entity.DocumentChunk;
import com.sourabh.document.exception.DuplicateDocumentException;
import com.sourabh.document.mapper.DocumentMapper;
import com.sourabh.document.repository.DocumentChunkRepository;
import com.sourabh.document.repository.DocumentRepository;
import com.sourabh.document.service.DocumentService;
import com.sourabh.document.service.FileStorageService;
import com.sourabh.document.service.TextExtractionService;
import com.sourabh.document.util.FileHashUtil;
import com.sourabh.vector.service.VectorStoreServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourabh.embedding.service.EmbeddingService;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository repository;
    private final FileStorageService storageService;
    private final DocumentMapper mapper;
    private final TextExtractionService textExtractionService;
    private final ChunkingServiceImpl chunkingService;
    private final DocumentChunkRepository documentChunkRepository;
    private final EmbeddingService embeddingService;
    private final ObjectMapper objectMapper;
    private final VectorStoreServiceImpl vectorStoreService;
    private final FileHashUtil fileHashUtil;

    @Override
    public DocumentResponse upload(MultipartFile file) {

        String path = storageService.store(file);

        String extractedText =
                textExtractionService.extract(
                        Paths.get(path)
                );

        String fileHash =
                fileHashUtil.generateSha256(
                        file
                );

        if (repository.existsByFileHash(fileHash)) {

            throw new DuplicateDocumentException(
                    "Document already exists"
            );
        }

        Document document =
                Document.builder()
                        .fileHash(fileHash)
                        .fileName(file.getOriginalFilename())
                        .fileType(file.getContentType())
                        .fileSize(file.getSize())
                        .filePath(path)
                        .extractedText(extractedText)
                        .uploadedAt(Instant.now())
                        .build();

        Document saved =
                repository.save(document);

        List<ChunkDto> chunks =
                chunkingService.chunk(extractedText);

        for (ChunkDto chunk : chunks) {

            List<Float> embedding =
                    embeddingService.generateEmbedding(
                            chunk.chunkText()
                    );

            String vectorId =
                    saved.getId() + "_" + chunk.chunkIndex();

            DocumentChunk documentChunk =
                    documentChunkRepository.save(
                            DocumentChunk.builder()
                                    .document(saved)
                                    .chunkIndex(chunk.chunkIndex())
                                    .chunkText(chunk.chunkText())
                                    .embedding(toJson(embedding))
                                    .vectorId(vectorId)
                                    .build()
                    );

            vectorStoreService.upsertChunk(
                    vectorId,
                    chunk.chunkText(),
                    saved.getId(),
                    saved.getFileName(),
                    chunk.chunkIndex(),
                    embedding
            );
        }

        return mapper.toResponse(saved);
    }

    @Override
    public String getExtractedText(Long id) {
        return repository.findById(id)
                .map(document -> textExtractionService.extract(
                        Paths.get(document.getFilePath())
                ))
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    private String toJson(Object object) {

        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to convert object to JSON",
                    e
            );
        }
    }
}