package com.sourabh.common.exception;

import com.sourabh.common.response.ApiResponse;
import com.sourabh.document.exception.DuplicateDocumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // =========================================================
    // VALIDATION EXCEPTION
    // =========================================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleValidationException(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors =
                new HashMap<>();

        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {

                    String fieldName =
                            ((FieldError) error)
                                    .getField();

                    String errorMessage =
                            error.getDefaultMessage();

                    errors.put(
                            fieldName,
                            errorMessage
                    );
                });

        ApiResponse<Object> response =
                ApiResponse.builder()
                        .success(false)
                        .message("Validation failed")
                        .data(null)
                        .errors(errors)
                        .build();

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    // =========================================================
    // BUSINESS EXCEPTION
    // =========================================================

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleBusinessException(
            BusinessException ex
    ) {

        ApiResponse<Object> response =
                ApiResponse.builder()
                        .success(false)
                        .message(ex.getMessage())
                        .data(null)
                        .errors(null)
                        .build();

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    // =========================================================
    // RESOURCE NOT FOUND
    // =========================================================

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleResourceNotFoundException(
            ResourceNotFoundException ex
    ) {

        ApiResponse<Object> response =
                ApiResponse.builder()
                        .success(false)
                        .message(ex.getMessage())
                        .data(null)
                        .errors(null)
                        .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    // =========================================================
    // UNAUTHORIZED
    // =========================================================

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Object>>
    handleUnauthorizedException(
            UnauthorizedException ex
    ) {

        ApiResponse<Object> response =
                ApiResponse.builder()
                        .success(false)
                        .message(ex.getMessage())
                        .data(null)
                        .errors(null)
                        .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    // =========================================================
    // GENERIC EXCEPTION
    // =========================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>>
    handleException(Exception ex) {

        log.error(
                "Unhandled exception occurred",
                ex
        );

        ApiResponse<Object> response =
                ApiResponse.builder()
                        .success(false)
                        .message(
                                "Internal server error"
                        )
                        .data(null)
                        .errors(null)
                        .build();

        return ResponseEntity
                .status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
                .body(response);
    }

    @ExceptionHandler(
            DuplicateDocumentException.class
    )
    public ResponseEntity<ApiResponse<?>>
    handleDuplicateDocument(
            DuplicateDocumentException ex
    ) {

        ApiResponse<?> response =
                ApiResponse.builder()
                        .success(false)
                        .message(ex.getMessage())
                        .data(null)
                        .errors(null)
                        .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }
}