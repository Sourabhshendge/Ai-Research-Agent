package com.sourabh.search.repository;

import com.sourabh.search.document.DocumentChunkIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentChunkSearchRepository
        extends ElasticsearchRepository<DocumentChunkIndex, String> {
}