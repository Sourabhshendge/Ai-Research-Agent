package com.sourabh.document.service;

import com.sourabh.document.dto.ChunkDto;

import java.util.List;

public interface ChunkingService {

    List<ChunkDto> chunk(String text);


}