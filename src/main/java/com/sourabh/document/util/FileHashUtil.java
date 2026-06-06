package com.sourabh.document.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.security.MessageDigest;

@Component
public class FileHashUtil {

    public String generateSha256(
            MultipartFile file
    ) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            try (InputStream is =
                         file.getInputStream()) {

                byte[] buffer = new byte[8192];

                int bytesRead;

                while ((bytesRead =
                        is.read(buffer))
                        != -1) {

                    digest.update(
                            buffer,
                            0,
                            bytesRead
                    );
                }
            }

            byte[] hash = digest.digest();

            StringBuilder sb =
                    new StringBuilder();

            for (byte b : hash) {

                sb.append(
                        String.format(
                                "%02x",
                                b
                        )
                );
            }

            return sb.toString();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to generate file hash",
                    e
            );
        }
    }
}