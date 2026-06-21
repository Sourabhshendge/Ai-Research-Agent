package com.sourabh.document.service;

import com.sourabh.document.dto.DocumentDetailsResponse;
import com.sourabh.document.dto.DocumentResponse;
import com.sourabh.document.dto.DocumentStatsResponse;
import com.sourabh.document.dto.ListDocumentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {

    DocumentResponse upload(MultipartFile file);

    String getExtractedText(Long id);

    List<ListDocumentResponse> getAllDocuments();

    DocumentDetailsResponse getDocument(Long id);


}