package com.sourabh.search.repository;

import com.sourabh.search.document.DocumentChunkIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentChunkSearchRepository
        extends ElasticsearchRepository<DocumentChunkIndex, String> {

    List<DocumentChunkIndex> findByDocumentId(Long documentId);


}