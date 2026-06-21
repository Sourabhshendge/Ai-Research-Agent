package com.sourabh.search.service;

import com.sourabh.document.entity.DocumentChunk;
import com.sourabh.document.repository.DocumentChunkRepository;
import com.sourabh.search.document.DocumentChunkIndex;
import com.sourabh.search.repository.DocumentChunkSearchRepository;
import com.sourabh.search.service.DocumentIndexingService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentIndexingServiceImpl
        implements DocumentIndexingService {

    private final DocumentChunkRepository documentChunkRepository;

    private final DocumentChunkSearchRepository
            searchRepository;

    @Override
    public void indexChunk(DocumentChunk chunk) {

        searchRepository.save(
                map(chunk)
        );
    }

    @Override
    public void indexChunks(
            List<DocumentChunk> chunks
    ) {

        List<DocumentChunkIndex> documents =
                chunks.stream()
                        .map(this::map)
                        .toList();

        searchRepository.saveAll(documents);
    }

    @Override
    @Transactional
    public void reindexAll() {

        System.out.println(
                "Deleting Elasticsearch index documents..."
        );

        searchRepository.deleteAll();

        List<DocumentChunk> chunks =
                documentChunkRepository
                        .findAllWithDocument();

        System.out.println(
                "Reindexing chunks: "
                        + chunks.size()
        );

        indexChunks(chunks);

        System.out.println(
                "Reindex completed"
        );
    }

    private DocumentChunkIndex map(
            DocumentChunk chunk
    ) {

        return DocumentChunkIndex.builder()
                .id(
                        String.valueOf(
                                chunk.getId()
                        )
                )
                .documentId(
                        chunk.getDocument().getId()
                )
                .chunkIndex(
                        chunk.getChunkIndex()
                )
                .fileName(
                        chunk.getDocument()
                                .getFileName()
                )
                .content(
                        chunk.getChunkText()
                )
                .build();
    }

    @Override
    public void deleteDocumentChunks(
            Long documentId
    ) {

        List<DocumentChunkIndex> chunks =
                searchRepository.findByDocumentId(
                        documentId
                );

        searchRepository.deleteAll(chunks);
    }
}