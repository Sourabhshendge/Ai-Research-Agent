package com.sourabh.document.service.impl;

import com.sourabh.document.dto.*;
import com.sourabh.document.entity.Document;
import com.sourabh.document.entity.DocumentStatus;
import com.sourabh.document.exception.DuplicateDocumentException;
import com.sourabh.document.mapper.DocumentMapper;
import com.sourabh.document.repository.DocumentChunkRepository;
import com.sourabh.document.repository.DocumentRepository;
import com.sourabh.document.service.DocumentService;
import com.sourabh.document.service.FileStorageService;
import com.sourabh.document.service.TextExtractionService;
import com.sourabh.document.util.FileHashUtil;
import com.sourabh.messaging.producer.DocumentUploadPublisher;
import com.sourabh.search.service.DocumentIndexingServiceImpl;
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
    private final DocumentIndexingServiceImpl indexingService;
    private final DocumentAsyncProcessor documentAsyncProcessor;
    private final DocumentUploadPublisher publisher;

    @Override
    public DocumentResponse upload(
            MultipartFile file
    ) {

        String fileHash =
                fileHashUtil.generateSha256(file);

        if (repository.existsByFileHash(fileHash)) {

            throw new DuplicateDocumentException(
                    "Document already exists"
            );
        }

        String path =
                storageService.store(file);

        Document document = Document.builder()
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .filePath(path)
                .fileSize(file.getSize())
                .uploadedAt(Instant.now())
                .fileHash(fileHash)
                .status(DocumentStatus.PENDING)
                .build();

        Document savedDocument =
                repository.save(document);

        publisher.publish(
                savedDocument.getId()
        );

        return mapper.toResponse(
                savedDocument
        );
    }

    @Override
    public List<ListDocumentResponse> getAllDocuments() {

        return repository.findAllByOrderByUploadedAtDesc()
                .stream()
                .map(this::toListResponse)
                .toList();
    }

    @Override
    public DocumentDetailsResponse getDocument(
            Long id
    ) {

        Document document =
                repository.findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Document not found"
                                )
                        );

        int chunkCount =
                documentChunkRepository.countByDocumentId(
                        id
                );

        return DocumentDetailsResponse.builder()
                .id(document.getId())
                .fileName(document.getFileName())
                .fileType(document.getFileType())
                .fileSize(document.getFileSize())
                .fileHash(document.getFileHash())
                .chunkCount(chunkCount)
                .uploadedAt(document.getUploadedAt())
                .build();
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

    private ListDocumentResponse toListResponse(
            Document document
    ) {

        int chunkCount =
                documentChunkRepository.countByDocumentId(
                        document.getId()
                );

        return ListDocumentResponse.builder()
                .id(document.getId())
                .fileName(document.getFileName())
                .fileType(document.getFileType())
                .fileSize(document.getFileSize())
                .chunkCount(chunkCount)
                .uploadedAt(document.getUploadedAt())
                .build();
    }
}