package com.sourabh.document.service.impl;

import com.sourabh.document.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path uploadPath;

    public FileStorageServiceImpl(
            @Value("${app.file.upload-dir:uploads}") String uploadDir
    ) throws IOException {

        this.uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }

    @Override
    public String store(MultipartFile file) {

        try {

            String fileName =
                    System.currentTimeMillis() + "_"
                            + StringUtils.cleanPath(
                            file.getOriginalFilename());

            Path target =
                    uploadPath.resolve(fileName);

            Files.copy(
                    file.getInputStream(),
                    target,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return target.toAbsolutePath()
                    .normalize()
                    .toString();

        } catch (IOException ex) {

            throw new RuntimeException(
                    "Failed to store file",
                    ex
            );
        }
    }

    @Override
    public Path getPath(String filePath) {
        return Paths.get(filePath);
    }

    @Override
    public void delete(String path) {

        try {

            Path filePath =
                    Paths.get(path)
                            .toAbsolutePath()
                            .normalize();

            System.out.println("PATH = " + filePath);
            System.out.println("EXISTS = " + Files.exists(filePath));

            boolean deleted =
                    Files.deleteIfExists(filePath);

            System.out.println("DELETED = " + deleted);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to delete file",
                    e
            );
        }
    }
}