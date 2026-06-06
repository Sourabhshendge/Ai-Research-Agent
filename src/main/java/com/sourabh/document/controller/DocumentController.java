package com.sourabh.document.controller;

import com.sourabh.common.response.ApiResponse;
import com.sourabh.document.dto.DocumentResponse;
import com.sourabh.document.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiResponse<DocumentResponse> upload(
            @RequestParam("file")
            MultipartFile file
    ) {

        return ApiResponse.success(
                "Document uploaded successfully",
                documentService.upload(file)
        );
    }

    @GetMapping("/{id}/text")
    public ApiResponse<String> getText(
            @PathVariable Long id
    ) {
        return ApiResponse.success(
                documentService.getExtractedText(id)
        );
    }

}