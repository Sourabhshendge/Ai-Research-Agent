    package com.sourabh.document.controller;

    import com.sourabh.common.response.ApiResponse;
    import com.sourabh.document.dto.DocumentDetailsResponse;
    import com.sourabh.document.dto.DocumentResponse;
    import com.sourabh.document.dto.DocumentStatsResponse;
    import com.sourabh.document.dto.ListDocumentResponse;
    import com.sourabh.document.service.DocumentManagementService;
    import com.sourabh.document.service.DocumentService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.MediaType;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;

    import java.util.List;

    @RestController
    @RequestMapping("/api/v1/documents")
    @RequiredArgsConstructor
    public class DocumentController {

        private final DocumentService documentService;
        private final DocumentManagementService
                managementService;

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

        @GetMapping
        public ApiResponse<List<ListDocumentResponse>> getDocuments() {

            return ApiResponse.success(
                    documentService.getAllDocuments()
            );
        }

        @GetMapping("/{id}")
        public ApiResponse<DocumentDetailsResponse> getDocument(
                @PathVariable Long id
        ) {

            return ApiResponse.success(
                    documentService.getDocument(id)
            );
        }

        @GetMapping("/stats")
        public ApiResponse<DocumentStatsResponse> getStats() {

            return ApiResponse.success(
                    managementService.getStats()
            );
        }

        @DeleteMapping("/{id}")
        public ApiResponse<Void> deleteDocument(
                @PathVariable Long id
        ) {

            managementService.deleteDocument(id);

            return ApiResponse.success(
                    "Document deleted successfully",
                    null
            );
        }

        @PostMapping("/{id}/reindex")
        public ApiResponse<Void> reindex(
                @PathVariable Long id
        ) {

            managementService.reindexDocument(
                    id
            );

            return ApiResponse.success(
                    "Document reindexed successfully",
                    null
            );
        }



    }