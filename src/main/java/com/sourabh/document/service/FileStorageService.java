package com.sourabh.document.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileStorageService {

    String store(MultipartFile file);

    Path getPath(String filePath);

    void delete(String path);
}