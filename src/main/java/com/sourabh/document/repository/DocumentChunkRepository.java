package com.sourabh.document.repository;

import com.sourabh.document.entity.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentChunkRepository
        extends JpaRepository<DocumentChunk, Long> {

    List<DocumentChunk> findByDocument_Id(Long documentId);

    @Query("""
       select dc
       from DocumentChunk dc
       join fetch dc.document
       """)
    List<DocumentChunk> findAllWithDocument();
}