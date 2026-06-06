package com.sourabh.document.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "documents")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    private String fileType;

    private String filePath;

    private Long fileSize;

    @Column(nullable = false)
    private Instant uploadedAt;

    @Column(nullable = false, unique = true, length = 64)
    private String fileHash;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String extractedText;
}