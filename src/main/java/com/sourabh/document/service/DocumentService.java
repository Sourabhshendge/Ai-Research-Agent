package com.sourabh.document.service;

import com.sourabh.document.dto.DocumentResponse;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {

    DocumentResponse upload(MultipartFile file);

    String getExtractedText(Long id);
}