package com.sourabh.retrieval.service;

import com.sourabh.retrieval.config.RetrievalProperties;
import com.sourabh.retrieval.dto.RetrievalEvaluationResponse;
import com.sourabh.retrieval.dto.RetrievedChunkDto;
import org.springframework.ai.document.Document;
import com.sourabh.retrieval.dto.SearchResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RetrievalServiceImpl
        implements RetrievalService {

    private final VectorStore vectorStore;
    private final RetrievalProperties retrievalProperties;

    @Override
    public List<SearchResultDto> search(
            String query,
            int topK
    ) {

        SearchRequest request =
                SearchRequest.builder()
                        .query(query)
                        .topK(topK)
                        .build();

        List<Document> documents =
                vectorStore.similaritySearch(request);

        return documents.stream()

                .map(doc ->
                        SearchResultDto.builder()
                                .content(doc.getText())
                                .score(Double.valueOf(doc.getScore()))
                                .vectorId(
                                        String.valueOf(
                                                doc.getMetadata().get("vectorId")
                                        )
                                )
                                .documentId(
                                        Long.parseLong(
                                                String.valueOf(
                                                        doc.getMetadata().get("documentId")
                                                )
                                        )
                                )
                                .fileName(
                                        String.valueOf(
                                                doc.getMetadata().get("fileName")
                                        )
                                )
                                .chunkIndex(
                                        Integer.parseInt(
                                                String.valueOf(
                                                        doc.getMetadata().get("chunkIndex")
                                                )
                                        )
                                )
                                .build()
                )

                .filter(result ->
                        result.getScore() >=
                                retrievalProperties.minScore()
                )

                .toList();
    }

    @Override
    public RetrievalEvaluationResponse evaluate(
            String question
    ) {

        List<SearchResultDto> results =
                search(question, 5);

        List<RetrievedChunkDto> chunks =
                results.stream()
                        .map(result ->
                                RetrievedChunkDto.builder()
                                        .documentId(result.getDocumentId())
                                        .fileName(result.getFileName())
                                        .chunkIndex(result.getChunkIndex())
                                        .score(result.getScore())
                                        .content(result.getContent())
                                        .build()
                        )
                        .toList();

        return RetrievalEvaluationResponse.builder()
                .question(question)
                .totalChunks(chunks.size())
                .results(chunks)
                .build();
    }
}