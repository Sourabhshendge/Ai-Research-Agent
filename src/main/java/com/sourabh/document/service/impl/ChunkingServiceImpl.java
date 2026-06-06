package com.sourabh.document.service.impl;

import com.sourabh.document.dto.ChunkDto;
import com.sourabh.document.service.ChunkingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChunkingServiceImpl
        implements ChunkingService {

    private static final int CHUNK_SIZE = 1000;

    private static final int OVERLAP = 200;

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