package com.sourabh.rerank.controller;

import com.sourabh.rerank.dto.CrossEncoderInput;
import com.sourabh.rerank.service.CrossEncoderInferenceService;
import com.sourabh.rerank.service.CrossEncoderTokenizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rerank")
@RequiredArgsConstructor
public class CrossEncoderScoreController {

    private final CrossEncoderTokenizerService tokenizerService;
    private final CrossEncoderInferenceService inferenceService;

    @GetMapping("/score")
    public String score() throws Exception {

        CrossEncoderInput input =
                tokenizerService.tokenize(
                        "What database is used?",
                        "The application uses MySQL database."
                );

        float score =
                inferenceService.score(input);

        return "Score = " + score;
    }
}
