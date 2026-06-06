package com.sourabh.document.service.impl;



import com.sourabh.document.service.TextExtractionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Slf4j
@Service
public class TextExtractionServiceImpl
        implements TextExtractionService {

    private final Tika tika = new Tika();

    @Override
    public String extract(Path filePath) {

        try {

            return tika.parseToString(
                    filePath
            );

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Failed to extract text",
                    ex
            );
        }
    }
}
