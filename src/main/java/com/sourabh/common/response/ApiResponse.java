package com.sourabh.common.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;

    private String message;

    private T data;

    private Object errors;

    // =========================================================
    // SUCCESS RESPONSE
    // =========================================================

    public static <T> ApiResponse<T> success(T data) {

        return ApiResponse.<T>builder()
                .success(true)
                .message("Success")
                .data(data)
                .errors(null)
                .build();
    }

    // =========================================================
    // SUCCESS RESPONSE WITH CUSTOM MESSAGE
    // =========================================================

    public static <T> ApiResponse<T> success(
            String message,
            T data
    ) {

        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .errors(null)
                .build();
    }

    // =========================================================
    // ERROR RESPONSE
    // =========================================================

    public static <T> ApiResponse<T> error(
            String message
    ) {

        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .errors(null)
                .build();
    }

    // =========================================================
    // ERROR RESPONSE WITH ERRORS
    // =========================================================

    public static <T> ApiResponse<T> error(
            String message,
            Object errors
    ) {

        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .errors(errors)
                .build();
    }
}