package com.sourabh.search.config;

import com.sourabh.search.service.DocumentIndexingService;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ElasticsearchBootstrapConfig {

    private final DocumentIndexingService
            indexingService;

    @Bean
    CommandLineRunner reindexRunner() {

        return args -> {

            indexingService.reindexAll();

            System.out.println(
                    "Elasticsearch reindex completed."
            );
        };
    }
}