package com.sourabh.retrieval.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.retrieval")
public record RetrievalProperties(
        int topK,
        double minScore
) {
}