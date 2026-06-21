package com.sourabh.document.repository;

import com.sourabh.document.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    boolean existsByFileHash(
            String fileHash
    );


    List<Document> findAllByOrderByUploadedAtDesc();

    @Query("""
       SELECT COALESCE(SUM(d.fileSize),0)
       FROM Document d
       """)
    Long getTotalStorageBytes();
}