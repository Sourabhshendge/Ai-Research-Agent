package com.sourabh.document.service;

import java.nio.file.Path;

public interface TextExtractionService {

    String extract(Path filePath);
}