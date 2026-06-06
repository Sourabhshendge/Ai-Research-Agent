package com.sourabh.chat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class CreateSessionRequest {

    @NotBlank
    private String title;
}