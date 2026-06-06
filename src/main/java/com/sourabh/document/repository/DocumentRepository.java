package com.sourabh.document.repository;

import com.sourabh.document.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    boolean existsByFileHash(
            String fileHash
    );
}