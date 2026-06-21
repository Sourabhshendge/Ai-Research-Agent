package com.sourabh.vector.service;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Points.Condition;
import io.qdrant.client.grpc.Points.FieldCondition;
import io.qdrant.client.grpc.Points.Filter;
import io.qdrant.client.grpc.Points.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QdrantCleanupServiceImpl
        implements QdrantCleanupService {

    private static final String COLLECTION_NAME =
            "document_chunks";

    private final QdrantClient qdrantClient;

    @Override
    public void deleteDocumentVectors(
            Long documentId
    ) {

        try {

            Filter filter = Filter.newBuilder()
                    .addMust(
                            Condition.newBuilder()
                                    .setField(
                                            FieldCondition.newBuilder()
                                                    .setKey("documentId")
                                                    .setMatch(
                                                            Match.newBuilder()
                                                                    .setKeyword(
                                                                            documentId.toString()
                                                                    )
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            qdrantClient.deleteAsync(
                    COLLECTION_NAME,
                    filter
            ).get();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to delete vectors for document: "
                            + documentId,
                    e
            );
        }
    }
}