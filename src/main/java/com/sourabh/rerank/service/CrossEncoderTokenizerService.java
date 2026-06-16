package com.sourabh.rerank.service;

import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import com.sourabh.rerank.dto.CrossEncoderInput;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CrossEncoderTokenizerService {

    private final HuggingFaceTokenizer tokenizer;

    public CrossEncoderTokenizerService() throws IOException {

        Path tokenizerPath =
                Paths.get(
                        "models",
                        "ms-marco-MiniLM-L-6-v2",
                        "tokenizer.json"
                );

        System.out.println(
                "Tokenizer path = "
                        + tokenizerPath.toAbsolutePath()
        );

        System.out.println(
                "Exists = "
                        + Files.exists(tokenizerPath)
        );

        this.tokenizer =
                HuggingFaceTokenizer.newInstance(
                        tokenizerPath
                );
    }

    public CrossEncoderInput tokenize(
            String query,
            String document
    ) {

        Encoding encoding =
                tokenizer.encode(query, document);

        long[] tokenTypeIds =
                encoding.getTypeIds();

        return new CrossEncoderInput(
                encoding.getIds(),
                encoding.getAttentionMask(),
                tokenTypeIds
        );
    }
}