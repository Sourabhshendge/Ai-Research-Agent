package com.sourabh.rerank.controller;

import com.sourabh.rerank.dto.CrossEncoderInput;
import com.sourabh.rerank.service.CrossEncoderTokenizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rerank")
@RequiredArgsConstructor
public class TokenizerTestController {

    private final CrossEncoderTokenizerService tokenizerService;

    @GetMapping("/tokenize")
    public CrossEncoderInput tokenize() {

        return tokenizerService.tokenize(
                "What database is used?",
                "The application uses MySQL database."
        );
    }
}
