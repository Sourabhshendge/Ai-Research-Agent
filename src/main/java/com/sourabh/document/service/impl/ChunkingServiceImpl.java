package com.sourabh.document.service.impl;

import com.sourabh.document.dto.ChunkDto;
import com.sourabh.document.service.ChunkingService;
import com.sourabh.search.document.DocumentChunkIndex;
import com.sourabh.search.repository.DocumentChunkSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChunkingServiceImpl
        implements ChunkingService {

    private final DocumentChunkSearchRepository searchRepository;

    private static final int CHUNK_SIZE = 300;

    private static final int OVERLAP = 50;

    @Override
    public List<ChunkDto> chunk(String text) {

        if (text == null || text.isBlank()) {
            return List.of();
        }

        List<ChunkDto> chunks = new ArrayList<>();

        int start = 0;
        int index = 0;

        while (start < text.length()) {

            int end =
                    Math.min(
                            start + CHUNK_SIZE,
                            text.length()
                    );

            chunks.add(
                    new ChunkDto(
                            index++,
                            text.substring(start, end)
                    )
            );

            start += (CHUNK_SIZE - OVERLAP);
        }

        return chunks;
    }

}